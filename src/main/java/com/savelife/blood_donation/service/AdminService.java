package com.savelife.blood_donation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.savelife.blood_donation.entity.BloodRequest;
import com.savelife.blood_donation.entity.RequestStatus;
import com.savelife.blood_donation.repository.BloodRequestRepository;
import com.savelife.blood_donation.repository.DonorProfileRepository;
import com.savelife.blood_donation.repository.UserRepository;

@Service
public class AdminService {
	private final BloodRequestRepository requests;
	private final DonorProfileRepository profiles;
	private final UserRepository users;

	public AdminService(BloodRequestRepository requests, DonorProfileRepository profiles, UserRepository users) {
		this.requests = requests;
		this.profiles = profiles;
		this.users = users;
	}

	@Transactional(readOnly = true)
	public List<BloodRequest> getRequests() {
		return requests.findAllByOrderByCreatedAtDesc();
	}

	@Transactional(readOnly = true)
	public long getDonorCount() {
		return profiles.count();
	}

	@Transactional(readOnly = true)
	public long getRequestCount() {
		return requests.count();
	}

	@Transactional
	public void updateRequestStatus(Long requestId, RequestStatus status) {
		BloodRequest request = requests.findById(requestId)
				.orElseThrow(() -> new IllegalArgumentException("Blood request was not found."));
		request.setStatus(status);
	}
}
