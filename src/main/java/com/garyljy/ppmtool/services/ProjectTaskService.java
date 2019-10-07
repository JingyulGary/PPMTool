package com.garyljy.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garyljy.ppmtool.domain.Backlog;
import com.garyljy.ppmtool.domain.Project;
import com.garyljy.ppmtool.domain.ProjectTask;
import com.garyljy.ppmtool.exceptions.ProjectNotFoundException;
import com.garyljy.ppmtool.repositories.BacklogRepository;
import com.garyljy.ppmtool.repositories.ProjectRepository;
import com.garyljy.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProjectService projectService;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
		

			//PTs to be added to a specific project, != null project, => BL exists
			Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);
			
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
	
		
		
	}

	public Iterable<ProjectTask> findBacklogById(String id, String sort, String username){
		
		projectService.findProjectByIdentifier(id, username);
		
		if(sort.equals("1"))
			return projectTaskRepository.findByProjectIdentifierOrderByDueDate(id);
		else if(sort.equals("2"))
			return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
		else
			return projectTaskRepository.findByProjectIdentifierOrderByProjectSequence(id);
	}

	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {
		
		//make sure we are searching on an existing backlog
		projectService.findProjectByIdentifier(backlog_id, username);
		
		//make sure that out task exists
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project Task '" + pt_id + "' not found");
		}
		
		//make sure we are searching on the right backlog
		if(!projectTask.getBacklog().getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task '" + pt_id + "' does not exist in project: '" + backlog_id);
		}
		
		return projectTask;
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		
		projectTask = updatedTask;
		
		return projectTaskRepository.save(projectTask);
	}
	
	public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		
		projectTaskRepository.delete(projectTask);
	}
}
