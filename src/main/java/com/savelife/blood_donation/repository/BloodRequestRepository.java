package com.savelife.blood_donation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.savelife.blood_donation.entity.BloodRequest;
import com.savelife.blood_donation.entity.RequestStatus;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
	@EntityGraph(attributePaths = "requester")
	List<BloodRequest> findAllByOrderByCreatedAtDesc();

	@EntityGraph(attributePaths = "requester")
	List<BloodRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);
}
