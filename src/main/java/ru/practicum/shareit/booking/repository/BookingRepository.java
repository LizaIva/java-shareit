package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("select b " +
            "   from bookings b left join items i on b.bookedItem.id = i.id " +
            "   where b.id = :bookingId and (b.booker.id = :userId or i.owner.id = :userId" +
            ")")
    Booking findByBookingIdAndIfUserOwnerOrBooker(@Param("bookingId") int bookingId, @Param("userId") int userId);

    @Query("select count(*) > 0 " +
            "   from bookings b left join items i on b.bookedItem.id = i.id " +
            "   where b.id = :bookingId and i.owner.id = :userId")
    Boolean isUserItemOwnerByBookingId(@Param("bookingId") int bookingId, @Param("userId") int userId);

    @Query(
            value = "select * from bookings b where b.booker_id = :bookerId order by b.start_date desc;",
            nativeQuery = true
    )
    List<Booking> getBookingsByStatus(
            @Param("bookerId") int bookerId
    );

    @Query(
            value = "select * from bookings b " +
                    "left join items i on b.item_id = i.item_id " +
                    "where i.owner_id = :ownerId order by b.start_date desc",
            nativeQuery = true
    )
    List<Booking> getBookingsByOwnerId(@Param("ownerId") int ownerId);

    @Query(
            value = "select * " +
                    "from bookings b " +
                    "where b.item_id = :itemId AND b.status_id = 1 " +
                    "order by b.start_date desc " +
                    "limit 1",
            nativeQuery = true
    )
    Booking findByLastTimeBooking(@Param("itemId") int itemId);

    @Query(
            value = "select * " +
                    "from bookings b " +
                    "where b.item_id = :itemId AND b.status_id = 1 " +
                    "order by b.start_date " +
                    "limit 1 offset 1",
            nativeQuery = true
    )
    Booking findNextTimeBooking(@Param("itemId") int itemId);


    @Query("select count(*) > 0 " +
            "   from bookings b " +
            "   where b.bookedItem.id = :itemId and b.booker.id = :userId and (b.status = 3 or current_timestamp > b.end)"
    )
    Boolean isUserWasBooker(@Param("itemId") int itemId, @Param("userId") int userId);
}
