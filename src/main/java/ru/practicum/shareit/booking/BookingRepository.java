package ru.practicum.shareit.booking;

import org.apache.tools.ant.types.resources.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdOrderByStartDesc(long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStateCurrentOrderByStartDesc(long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp > b.end " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStatePastOrderByStartDesc(long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp < b.start " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStateFutureOrderByStartDesc(long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus bookingStatus);

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findAllByBookerIdAndStateCurrentOrderByStartDesc(long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp > b.end " +
            "order by b.start desc")
    List<Booking> findAllByBookerIdAndStatePastOrderByStartDesc(long brokerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp < b.start " +
            "order by b.start desc")
    List<Booking> findAllByBookerIdAndStateFutureOrderByStartDesc(long bookerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus bookingStatus);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(long itemId,
                                                                             LocalDateTime localDateTime,
                                                                             BookingStatus bookingStatus);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByEndAsc(long itemId,
                                                                           LocalDateTime localDateTime,
                                                                           BookingStatus bookingStatus);

    Boolean existsByBookerIdAndItemIdAndEndBefore(long bookerId, long itemId, LocalDateTime localDateTime);
}
