package com.nokia.test.seatmanagement.dao;

import com.nokia.test.seatmanagement.entity.Company;
import com.nokia.test.seatmanagement.entity.Floor;
import com.nokia.test.seatmanagement.entity.Seat;
import com.nokia.test.seatmanagement.entity.SeatStatus;
import com.nokia.test.seatmanagement.exception.SeatAllocationException;
import com.nokia.test.seatmanagement.util.ERROR_CODE;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyDAO {
    private final List<Company> companies = new ArrayList<>();

    @PostConstruct
    public void loadSeats() {
        List<Floor> floors = new ArrayList<>();

        List<Seat> seats1 = new ArrayList<>();
        seats1.add(new Seat(1, 1, SeatStatus.UNAVAILABLE));
        seats1.add(new Seat(2, 1, SeatStatus.UNAVAILABLE));
        seats1.add(new Seat(3, 1, SeatStatus.AVAILABLE));
        Floor floor1 = new Floor(1, 1, seats1);

        List<Seat> seats2 = new ArrayList<>();
        seats2.add(new Seat(1, 2, SeatStatus.AVAILABLE));
        seats2.add(new Seat(2, 2, SeatStatus.AVAILABLE));
        seats2.add(new Seat(3, 2, SeatStatus.AVAILABLE));
        Floor floor2 = new Floor(2, 1, seats2);

        floors.add(floor1);
        floors.add(floor2);

        companies.add(new Company(1001, "Nokia", floors));
    }

    public Company getCompany(int companyId) {
        Optional<Company> company = companies.stream().filter(company1 -> company1.getId() == companyId).findFirst();
        if (company.isPresent())
            return company.get();
        throw new SeatAllocationException(ERROR_CODE.EC_002.getErrorMessage());
    }

    public List<Seat> getAvailableSeats(int companyId, int floorId) {
        Company company = getCompany(companyId);
        Optional<Floor> floors = company.getFloors().stream().filter(floor -> floor.getId() == floorId).findFirst();
        if (floors.isPresent()) {
            return floors.get().getSeats().stream()
                    .filter(seat -> seat.getSeatStatus() == SeatStatus.AVAILABLE).collect(Collectors.toList());
        }
        throw new SeatAllocationException(ERROR_CODE.EC_003.getErrorMessage());
    }

    public Seat getSeat(int companyId, int floorId, int seatId) {
        Company company = getCompany(companyId);
        Optional<Floor> floors = company.getFloors().stream().filter(floor -> floor.getId() == floorId).findFirst();
        if (floors.isPresent()) {
            Optional<Seat> seatOptional = floors.get().getSeats().stream().filter(seat -> seat.getId() == seatId).findFirst();
            if (seatOptional.isPresent()) {
                return seatOptional.get();
            }
            throw new SeatAllocationException(ERROR_CODE.EC_004.getErrorMessage());
        }
        throw new SeatAllocationException(ERROR_CODE.EC_003.getErrorMessage());
    }

    public boolean isSeatAvailable(int companyId, int floorId, int seatId) {
        return getAvailableSeats(companyId, floorId).stream().anyMatch(seat -> seat.getId() == seatId && seat.getSeatStatus() == SeatStatus.AVAILABLE);
    }

    public void updateSeatStatus(int companyId, int floorId, int seatId, SeatStatus seatStatus) {
        getCompany(companyId).getFloors().stream().filter(floor -> floor.getId() == floorId).
                findFirst().get().getSeats().forEach(seat -> {
            if (seat.getId() == seatId) {
                seat.setSeatStatus(seatStatus);
            }
        });
    }
}
