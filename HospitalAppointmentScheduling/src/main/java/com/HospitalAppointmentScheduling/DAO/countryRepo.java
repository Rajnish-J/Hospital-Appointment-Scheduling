package com.HospitalAppointmentScheduling.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HospitalAppointmentScheduling.Entity.countryVO;

public interface countryRepo extends JpaRepository<countryVO, Integer> {

}
