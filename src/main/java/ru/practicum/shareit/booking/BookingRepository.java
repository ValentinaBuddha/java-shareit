package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND current_timestamp BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerIdAndStateCurrentOrderByStartDesc(long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND current_timestamp > b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerIdAndStatePastOrderByStartDesc(long brokerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND current_timestamp < b.start " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerIdAndStateFutureOrderByStartDesc(long bookerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus bookingStatus);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdOrderByStartDesc(long ownerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND current_timestamp BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndStateCurrentOrderByStartDesc(long ownerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND current_timestamp < b.start " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndStateFutureOrderByStartDesc(long ownerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus bookingStatus);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND current_timestamp > b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndStatePastOrderByStartDesc(long ownerId);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(long itemId,
                                                                             LocalDateTime localDateTime,
                                                                             BookingStatus bookingStatus);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByEndAsc(long itemId,
                                                                           LocalDateTime localDateTime,
                                                                           BookingStatus bookingStatus);

    Boolean existsByBookerIdAndItemIdAndEndBefore(long bookerId, long itemId, LocalDateTime localDateTime);
}
