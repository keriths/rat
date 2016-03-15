package com.fs.rat.mng.service;

import com.fs.rat.mng.dto.PublishEnvironmentDTO;
import com.fs.rat.mng.dto.PublishProjectDTO;

import java.util.List;

/**
 * Created by fanshuai on 15/6/17.
 */
public interface PublishBaseDataService {

    void addPublishEnv(PublishEnvironmentDTO publishEnvironmentDTO)throws Exception;
    void editPublishEnv(PublishEnvironmentDTO publishEnvironmentDTO)throws Exception;
    List<PublishEnvironmentDTO> getPublishEnvs()throws Exception;

    void addPublishProject(PublishProjectDTO publishProjectDTO)throws Exception;
    void editPublishProject(PublishProjectDTO publishProjectDTO)throws Exception;
    List<PublishProjectDTO> getPublishProjects()throws Exception;



}
