package com.unirun.runner.repository;

import com.unirun.runner.entity.RunRecord;
import com.unirun.runner.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface RunRecordRepository extends JpaRepository<RunRecord, Long> {

    Page<RunRecord> findByUserOrderByStartTimeDesc(UserProfile user, Pageable pageable);

    long countByUser(UserProfile user);

    @Query("select coalesce(sum(r.distanceMeters),0) from RunRecord r where r.user = :user and r.startTime between :start and :end")
    Long sumDistanceByUserAndPeriod(@Param("user") UserProfile user,
                                    @Param("start") Instant start,
                                    @Param("end") Instant end);

    @Query("select count(distinct function('date', r.startTime)) from RunRecord r where r.user = :user")
    Long countActiveDays(@Param("user") UserProfile user);

    @Query("select function('date', r.startTime) as day, sum(r.distanceMeters) as total " +
            "from RunRecord r where r.user = :user and r.startTime between :start and :end " +
            "group by function('date', r.startTime) order by day")
    List<Object[]> aggregateDailyDistance(@Param("user") UserProfile user,
                                          @Param("start") Instant start,
                                          @Param("end") Instant end);

    @Query("select r.user.id as userId, r.user.nickname as nickname, r.user.avatarUrl as avatarUrl, r.user.school as school, sum(r.distanceMeters) as totalDistance " +
            "from RunRecord r where r.startTime between :start and :end and (:school is null or r.user.school = :school) " +
            "group by r.user.id, r.user.nickname, r.user.avatarUrl, r.user.school " +
            "order by sum(r.distanceMeters) desc")
    List<LeaderboardProjection> leaderboard(@Param("start") Instant start,
                                            @Param("end") Instant end,
                                            @Param("school") String school);

    interface LeaderboardProjection {
        Long getUserId();
        String getNickname();
        String getAvatarUrl();
        String getSchool();
        Long getTotalDistance();
    }
}
