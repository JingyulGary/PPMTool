package io.agileintelligence.ppmtool.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		
		try {
			//PTs to be added to a specific project, != null project, => BL exists
			Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
			
			//set backlog to the PT
			projectTask.setBacklog(backlog);
			
			//we want our project sequence to be like this: IDPRO-1	IDPRO-2
			Integer backlogSequence = backlog.getPTSequence();
			
			//Update the BL sequence
			backlogSequence++;
			backlog.setPTSequence(backlogSequence);
			
			//Add sequence to PT
			projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			
			//INITIAL Priority when priority null
			if(projectTask.getPriority() == null || projectTask.getPriority() == 0) {
				projectTask.setPriority(3);
			}
			
			//INITIAL status when status is null
			if(projectTask.getStatus() == null || projectTask.getStatus() == "") {
				projectTask.setStatus("TO_DO");
			}
			
			return projectTaskRepository.save(projectTask);
		} catch (Exception e) {
			throw new ProjectNotFoundException("Project Not Found");
		}
	
		
		
	}

	public Iterable<ProjectTask> findBacklogById(String id){
		
		Project project = projectRepository.findByProjectIdentifier(id);
		
		if(project == null) {
			throw new ProjectNotFoundException("Project with ID: '" + id + "' does not exist");
		}
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}

}
