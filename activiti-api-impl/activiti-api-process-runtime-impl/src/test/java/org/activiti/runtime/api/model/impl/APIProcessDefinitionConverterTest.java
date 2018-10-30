package org.activiti.runtime.api.model.impl;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.StartEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.activiti.runtime.api.model.impl.MockProcessDefinitionBuilder.processDefinitionBuilderBuilder;

public class APIProcessDefinitionConverterTest {

    @InjectMocks
    private APIProcessDefinitionConverter processDefinitionConverter;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void convertFromProcessDefinitionShouldSetAllFieldsInTheConvertedProcessDefinition() {
        ProcessDefinition convertedProcessDefinition = processDefinitionConverter.from(
                processDefinitionBuilderBuilder()
                        .withId("anId")
                        .withKey("processKey")
                        .withName("Process Name")
                        .withDescription("process description")
                        .withVersion(3)
                        .build()
        );

        //THEN
        assertThat(convertedProcessDefinition)
                .isNotNull()
                .extracting(ProcessDefinition::getId,
                            ProcessDefinition::getKey,
                            ProcessDefinition::getName,
                            ProcessDefinition::getDescription,
                            ProcessDefinition::getVersion)
                .containsExactly(
                             "anId",
                             "processKey",
                             "Process Name",
                             "process description",
                             3);
    }

    @Test
    public void convertFromProcessDefinitionShouldSetAllFieldsInTheConvertedProcessDefinitionAndFormKey() {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId("processKey");
        StartEvent startEvent = new StartEvent();
        startEvent.setFormKey("AFormKey");
        process.setInitialFlowElement(startEvent);
        model.addProcess(process);

        ProcessDefinition convertedProcessDefinition = processDefinitionConverter.fromExtractFormKey(
                processDefinitionBuilderBuilder()
                        .withId("anId")
                        .withKey("processKey")
                        .withName("Process Name")
                        .withDescription("process description")
                        .withVersion(3)
                        .build(),
        model);

        //THEN
        assertThat(convertedProcessDefinition)
                .isNotNull()
                .extracting(ProcessDefinition::getId,
                        ProcessDefinition::getKey,
                        ProcessDefinition::getName,
                        ProcessDefinition::getDescription,
                        ProcessDefinition::getVersion,
                        ProcessDefinition::getFormKey)
                .containsExactly(
                        "anId",
                        "processKey",
                        "Process Name",
                        "process description",
                        3,
                        "AFormKey");
    }
}
