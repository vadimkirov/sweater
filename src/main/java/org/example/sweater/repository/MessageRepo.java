package org.example.sweater.repository;

import org.example.sweater.domain.Message;
import org.example.sweater.domain.User;
import org.example.sweater.domain.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface MessageRepo extends CrudRepository<Message, Long> {

    @Query("select new org.example.sweater.domain.dto.MessageDto(" +
            "M, "+
            "COUNT(ML), "+
            "SUM(CASE WHEN ML = :user THEN 1 ELSE 0 END ) > 0"+
            ") "+
            "FROM Message AS M LEFT JOIN M.likes AS ML "+
            "WHERE M.tag = :tag " +
            "GROUP BY M")
    Page<MessageDto> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);

    @Query("select new org.example.sweater.domain.dto.MessageDto(" +
            "M, "+
            "COUNT(ML), "+
            "SUM(CASE WHEN ML = :user THEN 1 ELSE 0 END) > 0"+
            ") "+
            "FROM Message AS M LEFT JOIN M.likes AS ML "+
            "GROUP BY M")
    Page<MessageDto> findAll(Pageable pageable, @Param("user") User user);

    @Query("select new org.example.sweater.domain.dto.MessageDto(" +
            "M, "+
            "COUNT(ML), "+
            "SUM(CASE WHEN ML = :user THEN 1 ELSE 0 END ) > 0"+
            ") "+
            "FROM Message AS M LEFT JOIN M.likes AS ML "+
            "WHERE M.author = :author " +
            "GROUP BY M")
    Page<MessageDto> findByUser(Pageable pageable, @Param("author") User author, @Param("user") User user);
}
