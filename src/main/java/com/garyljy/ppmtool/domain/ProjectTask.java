package com.garyljy.ppmtool.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
public class ProjectTask {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;
	@Column(updatable = false, unique = true)
	@Getter @Setter
	private String projectSequence;
	@NotBlank(message = "Please include a project summary")
	@Getter @Setter
	private String summary;
	@Getter @Setter
	private String acceptanceCriteria;
	@Getter @Setter
	private String status;
	@Getter @Setter
	private Integer priority;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Getter @Setter
	private Date dueDate;
	//ManyToOne with Backlog
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "backlog_id", updatable = false, nullable = false)
	@JsonIgnore
	@Getter @Setter
	private Backlog backlog;
	
	@Column(updatable = false)
	@Getter @Setter
	private String projectIdentifier;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Getter @Setter
	private Date create_At;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Getter @Setter
	private Date update_At;
	
	public ProjectTask() {
		
	}
	
	@PrePersist
	protected void onCreate() {
		this.create_At = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.update_At = new Date();
	}

	
}
