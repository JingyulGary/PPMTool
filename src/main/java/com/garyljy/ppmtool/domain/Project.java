package com.garyljy.ppmtool.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;
	@NotBlank(message = "Project name is required")
	@Getter @Setter
	private String projectName;
	@NotBlank(message = "Project Identifier is required")
	@Size(min=4, max=5, message = "Please use 4 to 5 characters")
	@Column(updatable = false, unique =true)
	@Getter @Setter
	private String projectIdentifier;
	@NotBlank(message ="Project description is required")
	@Getter @Setter
	private String description;
	@JsonFormat(pattern = "yyyy-MM-dd") 
	@Getter @Setter
	private Date start_date;
	@JsonFormat(pattern = "yyyy-MM-dd") 
	@Getter @Setter
	private Date end_date;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(updatable = false)
	@Getter @Setter
	private Date created_At;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Getter @Setter
	private Date updated_At;
	
	@OneToOne(fetch =FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "project")
	@JsonIgnore
	@Getter @Setter
	private Backlog backlog;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@Getter @Setter
	private User user;
	
	@Getter @Setter
	private String projectLeader;
	
	@Getter @Setter
	private Integer sort;
	
	public Project() {
		sort = 0;
	}
	
	@PrePersist
	protected void onCreate() {
		this.created_At = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updated_At = new Date();
	}
}
