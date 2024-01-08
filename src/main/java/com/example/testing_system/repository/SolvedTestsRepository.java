package com.example.testing_system.repository;

import com.example.testing_system.model.SolvedTests;
import com.example.testing_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SolvedTestsRepository extends JpaRepository<SolvedTests, Integer> {
    List<SolvedTests> findTop5ByUserOrderByCreatedAtDesc(User user);

    Optional<SolvedTests> findSolvedTestsByIdAndUser(Integer id, User user);

    @Query("SELECT st FROM SolvedTests st WHERE st.createdAt BETWEEN :startDateTime AND :endDateTime AND st.user = :user")
    List<SolvedTests> findSolvedTestsByCreatedAtAndUser(
            @Param("startDateTime") Instant startDateTime,
            @Param("endDateTime") Instant endDateTime,
            @Param("user") User user
    );

    @Query(value = "select u.username from solved_tests st " +
            "join users u ON st.user_id = u.id " +
            "GROUP BY st.user_id, u.id " +
            "HAVING SUM(st.true_percentage) > SUM(st.false_percentage) " +
            "ORDER BY SUM(st.true_percentage) " +
            "DESC limit 3",
            nativeQuery = true)
    List<String> findTop3UsersByTruePercentage();
}
