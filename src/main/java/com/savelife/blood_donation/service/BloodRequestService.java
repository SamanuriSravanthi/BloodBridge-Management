package com.savelife.blood_donation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.savelife.blood_donation.dto.BloodRequestForm;
import com.savelife.blood_donation.entity.BloodRequest;
import com.savelife.blood_donation.entity.RequestStatus;
import com.savelife.blood_donation.entity.User;
import com.savelife.blood_donation.repository.BloodRequestRepository;
import com.savelife.blood_donation.repository.UserRepository;

@Service
public class BloodRequestService {
	private final BloodRequestRepository requests;
	private final UserRepository users;

	public BloodRequestService(BloodRequestRepository requests, UserRepository users) {
		this.requests = requests;
		this.users = users;
	}

	@Transactional(readOnly = true)
	public List<BloodRequest> getAllRequests() {
		return requests.findAllByOrderByCreatedAtDesc();
	}

	@Transactional(readOnly = true)
	public List<BloodRequest> getOpenRequests() {
		return requests.findByStatusOrderByCreatedAtDesc(RequestStatus.OPEN);
	}

	@Transactional
	public BloodRequest createRequest(String email, BloodRequestForm form) {
		User requester = users.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new IllegalStateException("Signed-in account could not be found."));
		BloodRequest request = new BloodRequest();
		request.setRequester(requester);
		request.setBloodGroup(form.getBloodGroup());
		request.setUnits(form.getUnits());
		request.setHospitalName(form.getHospitalName().trim());
		request.setCity(form.getCity().trim());
		request.setContactPhone(form.getContactPhone().trim());
		request.setMessage(form.getMessage() == null ? "" : form.getMessage().trim());
		return requests.save(request);
	}
}
