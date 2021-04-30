package com.swx.flowable.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInstanceVO {

    String processDefinitionKey;

    String processDefinitionName;

    String processDefinitionId;

    Date startTime;

}
