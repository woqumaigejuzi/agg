package com.swx.flowable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.swx.flowable.bean.entity.Person;
import com.swx.flowable.bean.vo.HistoricActivityInstanceVO;
import com.swx.flowable.bean.vo.ProcessInstanceVO;
import com.swx.flowable.bean.vo.TaskVO;
import com.swx.flowable.mapper.PersionMapper;
import com.swx.flowable.utils.EntityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.persistence.entity.DeploymentEntityImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Resource
    HistoryService historyService;


    @Autowired
    private PersionMapper persionMapper;

    @ApiOperation(value = "查询deploy列表")
    @GetMapping("/deploy")
    public List<Deployment> getDeploy() {
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> list = deploymentQuery
                .orderByDeploymentTime().desc()
                .list();
        list.forEach(x -> ((DeploymentEntityImpl) x).setResources(new HashMap<>()));
        return list;
    }

    @ApiOperation(value = "根据id删除deploy")
    @DeleteMapping("/deploy/{id}")
    public void deleteDeploy(@PathVariable String id) {
        repositoryService.deleteDeployment(id, true);
    }

    @ApiOperation(value = "启动一个process实例")
    @PostMapping(value = "/process")
    public void startProcessInstance(@RequestParam String assignee) {
        Person person = persionMapper.selectOne(new QueryWrapper<Person>().lambda().eq(Person::getUsername, assignee));
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("person", person);
        log.info("person:{}", person);
        ProcessInstance oneTaskProcess = runtimeService.startProcessInstanceByKey("oneTaskProcess", variables);
        log.info("启动process实例信息：{}", oneTaskProcess);
    }


    @ApiOperation(value = "process实例列表")
    @GetMapping("/instanceProcess")
    public List<ProcessInstanceVO> getInstanceProcess() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().orderByStartTime().asc().list();
        List<ProcessInstanceVO> vos = Lists.newArrayList();
        for (ProcessInstance processInstance : list) {
            ProcessInstanceVO processInstanceVO = EntityUtils.copyData(processInstance, ProcessInstanceVO.class);
        }
        list.forEach(i -> vos.add(EntityUtils.copyData(i, ProcessInstanceVO.class)));
        return vos;
    }


    @ApiOperation(value = "查询task列表")
    @GetMapping(value = "/task")
    public List<TaskVO> getTask(@RequestParam(required = false) String assigneeId) {
        List<TaskVO> vos = Lists.newArrayList();
        List<Task> list;
        if (StringUtils.isEmpty(assigneeId)) {
            list = taskService.createTaskQuery().list();
        } else {
            list = taskService.createTaskQuery().taskAssignee(assigneeId).list();
        }
        for (Task task : list) {
            TaskEntityImpl t = (TaskEntityImpl) task;
            vos.add(new TaskVO(t.getId(), t.getName(), t.getCreateTime()));
        }
        return vos;
    }

    @ApiOperation(value = "完成task任务")
    @PostMapping("/completeTask/{taskId}")
    public void completeTask(@PathVariable String taskId) {
        taskService.complete(taskId);
    }

    @ApiOperation(value = "查看各个时段的执行时间")
    @GetMapping("/historicActivityFlow")
    public Map<String, List<HistoricActivityInstanceVO>> getHistoricActivityFlow(@RequestParam String id) {
        List<HistoricActivityInstanceVO> list = Lists.newArrayList();
        List<HistoricActivityInstance> activities =
                historyService.createHistoricActivityInstanceQuery().orderByActivityId().asc().list();
        for (HistoricActivityInstance ac : activities) {
            HistoricActivityInstanceVO vo = new HistoricActivityInstanceVO(ac.getProcessInstanceId(), ac.getActivityId(), ac.getActivityName(), ac.getDurationInMillis());
            list.add(vo);
        }
        Map<String, List<HistoricActivityInstanceVO>> map = list.stream().collect(Collectors.groupingBy(HistoricActivityInstanceVO::getProcessInstanceId));
        return map;
    }


    @ApiOperation(value = "查询person列表")
    @GetMapping("/person")
    public List<Person> getPerson() {
        return persionMapper.selectList(null);
    }

}
