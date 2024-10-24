package com.HospitalAppointmentScheduling.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HospitalAppointmentScheduling.CustomExceptions.AppointmentException;
import com.HospitalAppointmentScheduling.CustomExceptions.DateException;
import com.HospitalAppointmentScheduling.CustomExceptions.DateOfBirthException;
import com.HospitalAppointmentScheduling.CustomExceptions.EmailException;
import com.HospitalAppointmentScheduling.CustomExceptions.IdException;
import com.HospitalAppointmentScheduling.CustomExceptions.PasswordException;
import com.HospitalAppointmentScheduling.CustomExceptions.PatientException;
import com.HospitalAppointmentScheduling.CustomExceptions.PhoneNumberException;
import com.HospitalAppointmentScheduling.DTO.AppointmentDTO;
import com.HospitalAppointmentScheduling.DTO.AppointmentPatientDoctorAppointmentStatusDTO;
import com.HospitalAppointmentScheduling.Entity.AppointmentStatusVO;
import com.HospitalAppointmentScheduling.Entity.AppointmentsVO;
import com.HospitalAppointmentScheduling.Entity.DoctorVO;
import com.HospitalAppointmentScheduling.Entity.PatientVO;
import com.HospitalAppointmentScheduling.Response.ResponseHandle;
import com.HospitalAppointmentScheduling.Response.ResponseHandleAppointments;
import com.HospitalAppointmentScheduling.Service.AppointmentsService;
import com.HospitalAppointmentScheduling.Service.PatientService;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentsService aser;

	@Autowired
	private PatientService pser;

	@Autowired
	private ResponseHandleAppointments apptRes;

	@Autowired
	private ResponseHandle patRes;

	Logger log = Logger.getLogger(AppointmentController.class);

	// insert:
	@PostMapping("/insert")
	public ResponseEntity<?> insertAppointment(@RequestBody AppointmentPatientDoctorAppointmentStatusDTO dto) {
		log.info("Appointment booking with patient details method triggered in controller layer...");

		// converting DTO to entity
		DoctorVO dVO = new DoctorVO();
		dVO.setDoctorId(dto.getDoctor().getDoctorId());

		AppointmentStatusVO asVO = new AppointmentStatusVO();
		asVO.setStatusName("Pending");

		AppointmentsVO vo = new AppointmentsVO();
		vo.setAppointmentDate(dto.getAppointment().getAppointmentDate());
		vo.setDoctor(dVO);
		vo.setStatus(asVO);
		vo.setReason(dto.getAppointment().getReason());

		PatientVO vo1 = new PatientVO();
		vo1.setFirstName(dto.getPatient().getFirstName());
		vo1.setLastName(dto.getPatient().getLastName());
		vo1.setPatientEmail(dto.getPatient().getPatientEmail());
		vo1.setPatientPassword(dto.getPatient().getPatientPassword());
		vo1.setPatientPhone(dto.getPatient().getPatientPhone());
		vo1.setDob(dto.getPatient().getDob());

		try {
			patRes = pser.insertPatientDetails(vo1);
		} catch (PatientException e) {
			log.error("Patient Details details records not in the format", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PhoneNumberException e) {
			log.error("Phone number format is wrong", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (EmailException e) {
			log.error("email format is not valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PasswordException e) {
			log.error("password format is not valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (DateOfBirthException e) {
			log.error("Date of Birth format is not in the pattern", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		vo.setPatient(patRes.getPatient());
		log.info("Assigned Patient to Appointment VO.");

		log.info("Attempting to insert appointment details...");
		try {
			apptRes = aser.insertAppointments(vo);
		} catch (IdException e) {
			log.error("Appointment ID is not present in the DataBase", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (EmailException e) {
			log.error("email Exception caught valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PasswordException e) {
			log.error("password format is not valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PhoneNumberException e) {
			log.error("Phone number Exception caught", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (AppointmentException e) {
			log.error("Appointment exception caught", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PatientException e) {
			log.error("Patient Exception caught", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (DateOfBirthException e) {
			log.error("Date of Birth exception caught", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		dto.getAppointment().setAppointmentID(vo.getAppointmentID());
		dto.getAppointment().setCreatedAt(vo.getCreatedAt());
		dto.getAppointment().setUpdatedAt(vo.getUpdatedAt());
		return ResponseEntity.ok("Appointments Details successfully saved: " + apptRes.getAppoVo().getAppointmentID());
	}

	// insert appointments with patient ID
	@PostMapping("/insertWithPatientID")
	public ResponseEntity<?> insertAppointmentsWithPatientID(
			@RequestBody AppointmentPatientDoctorAppointmentStatusDTO dto) {
		log.info("Appointment booking with patient ID method triggered in controller layer...");

		// converting DTO to entity
		DoctorVO dVO = new DoctorVO();
		dVO.setDoctorId(dto.getDoctor().getDoctorId());

		AppointmentStatusVO asVO = new AppointmentStatusVO();
		asVO.setStatusName("Pending");

		AppointmentsVO vo = new AppointmentsVO();
		vo.setAppointmentDate(dto.getAppointment().getAppointmentDate());
		vo.setDoctor(dVO);
		vo.setStatus(asVO);
		vo.setReason(dto.getAppointment().getReason());

		PatientVO vo1 = new PatientVO();
		vo1.setPatientId(dto.getPatient().getPatientId());
		vo.setPatient(vo1);

		log.info("Assigned Patient ID to Appointment VO");

		log.info("Attempting to insert appointment details with existing patient ID...");
		try {
			apptRes = aser.insertAppointmentsWithPatientID(vo);
		} catch (IdException e) {
			log.error("Appointment ID is not present in the DataBase", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (EmailException e) {
			log.error("email Exception caught valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PasswordException e) {
			log.error("password format is not valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PhoneNumberException e) {
			log.error("Phone number Exception caught", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (AppointmentException e) {
			log.error("Appointment exception caught", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PatientException e) {
			log.error("Patient Exception caught", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (DateOfBirthException e) {
			log.error("Date of Birth exception caught", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		dto.getAppointment().setAppointmentID(vo.getAppointmentID());
		dto.getAppointment().setCreatedAt(vo.getCreatedAt());
		dto.getAppointment().setUpdatedAt(vo.getUpdatedAt());
		return ResponseEntity.ok("Appointments Details successfully saved: " + apptRes.getAppoVo().getAppointmentID());

	}

	// fetch by ID:
	@GetMapping("appointmentID/{id}")
	public ResponseEntity<?> findByappointmentId(@PathVariable("id") long id) {
		log.info("appointment details by their ID...");
		try {
			String pass = "Fetching appointment details by ID: " + id;
			log.info(pass);
			apptRes = aser.fetchByID(id);
			return ResponseEntity.ok("Appointment Details Fetched by ID:" + apptRes.getAppoVo());
		} catch (IdException e) {
			log.error("ID not found in the DateBase", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	// fetch all:
	@GetMapping("/fetchallAppointments")
	public List<AppointmentDTO> fetchall() {
		log.info("appointment chooses fetch all the details option...");
		apptRes = aser.fetchAll();
		List<AppointmentsVO> list = apptRes.getList();
		List<AppointmentDTO> listd = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			AppointmentsVO vo = list.get(i);
			AppointmentDTO getDto = mapToDTO(vo);
			listd.add(getDto);
		}
		log.info("All appointment details fetched successfully.");
		return listd;

	}

	// update method:
	@PutMapping("/updateAppointments/{id}")
	public ResponseEntity<?> updateAppointmentDetails(@PathVariable long id) {
		log.info("Appointment details update method triggerred");

		try {
			apptRes = aser.update(id);
			String pass = "Appointment ID: \" + apptRes.getAppoVo().getAppointmentID() + \" updated successfully.";
			log.info(pass);
			return ResponseEntity.ok("Appointment ID: " + apptRes.getAppoVo().getAppointmentID() + " updated");
		} catch (IdException e) {
			log.error("ID not found in the DataBase", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Appointment by between two days:
	@GetMapping("/AppointmentDetailsAmongTwoDate/{sd}/{ld}")
	public ResponseEntity<?> betweenTwoDOBpat(@PathVariable("sd") LocalDate sd, @PathVariable("ld") LocalDate ld) {
		log.info("Appointment details with the two dates...");
		try {
			String pass = "Fetching between start date: " + sd + " and end date: " + ld;
			log.info(pass);
			apptRes = aser.fetchApptBetweenTwoDates(sd, ld);
			List<AppointmentsVO> list = apptRes.getList();
			List<AppointmentDTO> listd = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				AppointmentsVO vo = list.get(i);
				AppointmentDTO getDto = mapToDTO(vo);
				listd.add(getDto);
				log.info("Fetched appointment details between two dates successfully.");
			}
		} catch (DateException e) {
			log.error("Id Exception", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		return ResponseEntity.ok("Appointment detials fetched successfully" + apptRes.getList());

	}

	// ascending order:
	@GetMapping("/AppointmentAscendingOrder")
	public ResponseEntity<?> acending() {
		log.info("Appointments fetching all the details in ascending order...");
		try {
			apptRes = aser.acending();
			List<AppointmentsVO> list = apptRes.getList();
			List<AppointmentDTO> listd = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				AppointmentsVO vo = list.get(i);
				AppointmentDTO getDto = mapToDTO(vo);
				listd.add(getDto);
			}
		} catch (AppointmentException e) {
			log.error("Id Exception", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		return ResponseEntity.ok("Ascending order detials fetched successfully" + apptRes.getList());

	}

	// Vo to DTO method
	public static AppointmentDTO mapToDTO(AppointmentsVO vo) {
		AppointmentDTO dto = new AppointmentDTO();
		dto.setAppointmentDate(vo.getAppointmentDate());
		dto.setAppointmentID(vo.getAppointmentID());
//		dto.setDoctor(vo.getDoctor());
		dto.setReason(vo.getReason());
		dto.setCreatedAt(vo.getCreatedAt());
		dto.setUpdatedAt(vo.getUpdatedAt());

		return dto;
	}

}
