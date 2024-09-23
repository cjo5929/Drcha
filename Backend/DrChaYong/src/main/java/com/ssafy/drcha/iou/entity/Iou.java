package com.ssafy.drcha.iou.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity
@Table(name = "iou")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Iou {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loan_agreement_id")
	private Long iouId;

	@Column(name = "creditor_id")
	private Long creditorId;

	@Column(name = "debtor_id")
	private Long debtorId;
}