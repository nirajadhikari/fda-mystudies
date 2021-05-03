/*
 * Copyright 2020 Google LLC
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file
 * or at https://opensource.org/licenses/MIT.
 */

package com.google.cloud.healthcare.fdamystudies.bean;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ActivityStateRequestBean {

  @NotBlank private String participantId;
  @NotBlank private String studyId;
  private List<ParticipantActivityBean> activity = new ArrayList<>();
  private String message = null;
}
