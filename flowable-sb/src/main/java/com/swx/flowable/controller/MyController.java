package com.swx.flowable.controller;

import com.swx.flowable.bean.entity.Persion;
import com.swx.flowable.bean.vo.TaskVO;
import com.swx.flowable.mapper.PersionMapper;
import com.swx.flowable.service.MyService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.repository.EngineResource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.DeploymentEntityImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentQuery;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Api(tags = {"流程管理接口"})
@Slf4j
@RestController
@RequestMapping("/fm")
public class MyController {

    @Resource
    RepositoryService repositoryService;
    @Resource
    TaskService taskService;
    @Resource
    RuntimeService runtimeService;


    @Autowired
    private PersionMapper persionMapper;

    @GetMapping("/deploy")
    public List<Deployment> getDeploy() {
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> list = deploymentQuery
                .orderByDeploymentTime().desc()
                .list();
        list.forEach(x -> {
            ((DeploymentEntityImpl) x).setResources(new HashMap<>());
        });
        return list;
    }

    @DeleteMapping("/deploy/{id}")
    public void deleteDeploy(@PathVariable String id) {
        repositoryService.deleteDeployment(id, true);
    }


    @PostMapping(value = "/startProcessInstance")
    public void startProcessInstance() {
        return;
    }

    @GetMapping(value = "/task")
    public List<TaskVO> getTask(@RequestParam String assignee) {
        return null;
    }

    @GetMapping("/persion")
    public List<Persion> getPersion() {
        return persionMapper.selectList(null);
    }


}
