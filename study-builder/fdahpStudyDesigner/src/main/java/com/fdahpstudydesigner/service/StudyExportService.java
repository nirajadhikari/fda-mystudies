package com.fdahpstudydesigner.service;

import com.fdahpstudydesigner.bean.QuestionnairesStepsIdsBean;
import com.fdahpstudydesigner.bo.ActiveTaskAtrributeValuesBo;
import com.fdahpstudydesigner.bo.ActiveTaskBo;
import com.fdahpstudydesigner.bo.ActiveTaskCustomScheduleBo;
import com.fdahpstudydesigner.bo.ActiveTaskFrequencyBo;
import com.fdahpstudydesigner.bo.AnchorDateTypeBo;
import com.fdahpstudydesigner.bo.ComprehensionTestQuestionBo;
import com.fdahpstudydesigner.bo.ComprehensionTestResponseBo;
import com.fdahpstudydesigner.bo.ConsentBo;
import com.fdahpstudydesigner.bo.ConsentInfoBo;
import com.fdahpstudydesigner.bo.EligibilityBo;
import com.fdahpstudydesigner.bo.EligibilityTestBo;
import com.fdahpstudydesigner.bo.FormBo;
import com.fdahpstudydesigner.bo.FormMappingBo;
import com.fdahpstudydesigner.bo.InstructionsBo;
import com.fdahpstudydesigner.bo.NotificationBO;
import com.fdahpstudydesigner.bo.QuestionReponseTypeBo;
import com.fdahpstudydesigner.bo.QuestionResponseSubTypeBo;
import com.fdahpstudydesigner.bo.QuestionnaireBo;
import com.fdahpstudydesigner.bo.QuestionnaireCustomScheduleBo;
import com.fdahpstudydesigner.bo.QuestionnairesFrequenciesBo;
import com.fdahpstudydesigner.bo.QuestionnairesStepsBo;
import com.fdahpstudydesigner.bo.QuestionsBo;
import com.fdahpstudydesigner.bo.ResourceBO;
import com.fdahpstudydesigner.bo.StudyBo;
import com.fdahpstudydesigner.bo.StudyPageBo;
import com.fdahpstudydesigner.bo.StudyPermissionBO;
import com.fdahpstudydesigner.bo.StudySequenceBo;
import com.fdahpstudydesigner.dao.NotificationDAO;
import com.fdahpstudydesigner.dao.StudyActiveTasksDAO;
import com.fdahpstudydesigner.dao.StudyDAO;
import com.fdahpstudydesigner.dao.StudyQuestionnaireDAO;
import com.fdahpstudydesigner.util.FdahpStudyDesignerUtil;
import com.fdahpstudydesigner.util.IdGenerator;
import com.fdahpstudydesigner.util.StudyExportSqlQueries;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudyExportService {

  private static Logger logger = Logger.getLogger(StudyExportService.class.getName());

  @Autowired private StudyDAO studyDao;

  @Autowired private StudyQuestionnaireDAO studyQuestionnaireDAO;

  @Autowired private NotificationDAO notificationDAO;

  @Autowired private StudyActiveTasksDAO studyActiveTasksDAO;

  private static final String PATH_SEPARATOR = "/";

  public String exportStudy(String studyId, String userId) throws IOException {

    List<String> insertSqlStatements = new ArrayList<>();

    StudyBo studyBo = studyDao.getStudy(studyId);
    Map<String, String> map = FdahpStudyDesignerUtil.getAppProperties();
    String newStudyId = IdGenerator.id();
    String newCustomId = studyBo.getCustomStudyId() + "_" + map.get("studyVersion");

    StudyPermissionBO studyPermissionBo = studyDao.getStudyPermissionBO(studyBo.getId(), userId);

    StudySequenceBo studySequenceBo = studyDao.getStudySequenceByStudyId(studyBo.getId());
    AnchorDateTypeBo anchorDate = studyDao.getAnchorDateDetails(studyBo.getId());

    List<StudyPageBo> studypageList = studyDao.getOverviewStudyPagesById(studyBo.getId(), userId);

    EligibilityBo eligibilityBo = studyDao.getStudyEligibiltyByStudyId(studyBo.getId());
    String newEligibilityId = IdGenerator.id();

    List<EligibilityTestBo> eligibilityBoList = null;
    if (eligibilityBo != null) {
      eligibilityBoList = studyDao.viewEligibilityTestQusAnsByEligibilityId(eligibilityBo.getId());
    }

    List<ConsentBo> consentBoList = studyDao.getConsentList(studyBo.getId());

    List<ConsentInfoBo> consentInfoBoList = studyDao.getConsentInfoList(studyBo.getId());

    List<ComprehensionTestQuestionBo> comprehensionTestQuestionBoList =
        studyDao.getComprehensionTestQuestionList(studyBo.getId());

    List<String> comprehensionTestQuestionIds = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(comprehensionTestQuestionBoList)) {
      for (ComprehensionTestQuestionBo comprehensionTestQuestionBo :
          comprehensionTestQuestionBoList) {
        comprehensionTestQuestionIds.add(comprehensionTestQuestionBo.getId());
      }
    }

    List<ComprehensionTestResponseBo> comprehensionTestResponseBoList =
        studyDao.getComprehensionTestResponseList(comprehensionTestQuestionIds);

    List<QuestionnaireBo> questionnairesList =
        studyQuestionnaireDAO.getStudyQuestionnairesByStudyId(studyBo.getId());

    List<String> questionnaireIds = new ArrayList<>();
    List<String> newQuestionnaireIds = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(questionnairesList)) {
      for (QuestionnaireBo questionnaireBo : questionnairesList) {
        questionnaireIds.add(questionnaireBo.getId());
        newQuestionnaireIds.add(IdGenerator.id());
      }
    }

    List<QuestionnairesStepsBo> questionnairesStepsList =
        studyQuestionnaireDAO.getQuestionnairesStepsList(questionnaireIds);

    List<String> instructionFormIds = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(questionnairesStepsList)) {
      for (QuestionnairesStepsBo questionnairesStepsBo : questionnairesStepsList) {
        instructionFormIds.add(questionnairesStepsBo.getInstructionFormId());
      }
    }

    List<QuestionnairesFrequenciesBo> questionnairesFrequenciesBoList =
        studyQuestionnaireDAO.getQuestionnairesFrequenciesBoList(questionnaireIds);

    List<QuestionnaireCustomScheduleBo> questionnairesCustomFrequenciesBoList =
        studyQuestionnaireDAO.getQuestionnairesCustomFrequenciesBoList(questionnaireIds);

    List<QuestionsBo> questionsList =
        studyQuestionnaireDAO.getQuestionsByInstructionFormIds(instructionFormIds);

    List<FormMappingBo> formMappingList =
        studyQuestionnaireDAO.getFormMappingbyInstructionFormIds(instructionFormIds);

    List<FormBo> formsList = studyQuestionnaireDAO.getFormsByInstructionFormIds(instructionFormIds);

    List<InstructionsBo> instructionList =
        studyQuestionnaireDAO.getInstructionListByInstructionFormIds(instructionFormIds);

    List<QuestionResponseSubTypeBo> questionResponseSubTypeBoList =
        studyQuestionnaireDAO.getQuestionResponseSubTypeBoByInstructionFormIds(instructionFormIds);

    List<QuestionReponseTypeBo> questionResponseTypeBo =
        studyQuestionnaireDAO.getQuestionResponseTypeBoByInstructionFormIds(instructionFormIds);

    List<NotificationBO> notificationBOs = notificationDAO.getNotificationList(studyBo.getId());

    List<ResourceBO> resourceBOs = studyDao.getResourceList(studyBo.getId());

    List<ActiveTaskBo> activeTaskBos =
        studyActiveTasksDAO.getStudyActiveTaskByStudyId(studyBo.getId());

    List<String> activeTaskIds = new ArrayList<>();
    List<String> newActiveTaskIds = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(activeTaskBos)) {
      for (ActiveTaskBo activeTaskBo : activeTaskBos) {
        activeTaskIds.add(activeTaskBo.getId());
        newActiveTaskIds.add(IdGenerator.id());
      }
    }

    List<ActiveTaskAtrributeValuesBo> activeTaskAtrributeValuesBos =
        studyActiveTasksDAO.getActiveTaskAtrributeValuesByActiveTaskId(activeTaskIds);

    List<ActiveTaskCustomScheduleBo> activeTaskCustomScheduleBoList =
        studyActiveTasksDAO.getActiveTaskCustomScheduleBoList(activeTaskIds);

    List<ActiveTaskFrequencyBo> activeTaskFrequencyBoList =
        studyActiveTasksDAO.getActiveTaskFrequencyBoList(activeTaskIds);

    List<String> questionFormInstructionIds = new ArrayList<>();
    QuestionnairesStepsIdsBean questionnairesStepsIdsBean =
        getInstructionFormIds(
            questionsList,
            formMappingList,
            instructionList,
            formsList,
            questionResponseSubTypeBoList,
            questionResponseTypeBo,
            questionFormInstructionIds);

    try {
      addStudiesInsertSql(studyBo, insertSqlStatements, newStudyId, newCustomId);
      addStudyPermissionInsertQuery(studyPermissionBo, insertSqlStatements, newStudyId);
      addStudySequenceInsertSql(studySequenceBo, insertSqlStatements, newStudyId);
      addAnchorDateInsertSql(anchorDate, insertSqlStatements, newStudyId, newCustomId);
      addStudypagesListInsertSql(studypageList, insertSqlStatements, newStudyId);
      addEligibilityInsertSql(eligibilityBo, insertSqlStatements, newEligibilityId, newStudyId);
      addEligibilityTestListInsertSql(eligibilityBoList, insertSqlStatements, newEligibilityId);
      addConsentBoListInsertSql(consentBoList, insertSqlStatements, newStudyId, newCustomId);

      addConsentInfoBoListInsertSql(
          consentInfoBoList, insertSqlStatements, newStudyId, newCustomId);

      addComprehensionTestQuestionListInsertSql(
          comprehensionTestQuestionBoList, insertSqlStatements, newStudyId);
      addComprehensionTestResponseBoListInsertSql(
          comprehensionTestResponseBoList, insertSqlStatements);

      addQuestionnaireBoListInsertSql(
          questionnairesList, insertSqlStatements, newStudyId, newCustomId, newQuestionnaireIds);

      addQuestionnaireFrequenciesBoInsertSql(
          questionnairesFrequenciesBoList, insertSqlStatements, newQuestionnaireIds);

      addQuestionnaireCustomScheduleBoInsertSql(
          questionnairesCustomFrequenciesBoList, insertSqlStatements, newQuestionnaireIds);

      addQuestionListInsertSql(
          questionsList, insertSqlStatements, questionnairesStepsIdsBean.getQuestionIds());

      addFormMappingListInsertSql(
          formMappingList, insertSqlStatements, questionnairesStepsIdsBean.getFormMappingIds());

      addFormsListInsertSql(
          formsList, insertSqlStatements, questionnairesStepsIdsBean.getFormsIds());

      addInstructionInsertSql(
          instructionList, insertSqlStatements, questionnairesStepsIdsBean.getInstructionIds());

      addQuestionsResponseSubTypeInsertSql(
          questionResponseSubTypeBoList,
          insertSqlStatements,
          questionnairesStepsIdsBean.getQuestionResponseSubTypeIds());

      addQuestionsResponseTypeInsertSql(
          questionResponseTypeBo,
          insertSqlStatements,
          questionnairesStepsIdsBean.getQuestionResponseIds());

      addQuestionnairesStepsListInsertSql(
          questionnairesStepsList,
          insertSqlStatements,
          newQuestionnaireIds,
          questionFormInstructionIds);

      addStudyActiveTaskInsertSql(
          activeTaskBos, insertSqlStatements, newActiveTaskIds, newStudyId, newCustomId);
      addActiveTaskAtrributeValuesInsertSql(
          activeTaskAtrributeValuesBos, insertSqlStatements, newActiveTaskIds);
      addActiveTaskCustomScheduleBoInsertSqlQuery(
          activeTaskCustomScheduleBoList, insertSqlStatements, newActiveTaskIds);
      addActiveTaskFrequencyBoInsertSqlQuery(
          activeTaskFrequencyBoList, insertSqlStatements, newActiveTaskIds);

      addNotificationInsertSql(
          notificationBOs,
          insertSqlStatements,
          newStudyId,
          newCustomId,
          newQuestionnaireIds,
          newActiveTaskIds);

      addResourceInsertSql(resourceBOs, insertSqlStatements, newStudyId);

    } catch (SQLException e) {
      logger.error(String.format("export study failed due to %s", e.getMessage()), e);
    }
    for (String sql : insertSqlStatements) {
      System.out.println(sql);
    }
    return saveFile(studyBo, insertSqlStatements);
  }

  private void addFormsListInsertSql(
      List<FormBo> formsList, List<String> insertSqlStatements, List<String> formsIds)
      throws SQLException {
    List<String> formBoInsertQueryList = new ArrayList<>();
    if (CollectionUtils.isEmpty(formsList)) {
      return;
    }

    String formInsertQuery = null;
    for (FormBo formBo : formsList) {
      for (String formId : formsIds) {
        formInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.FORM,
                formId,
                formBo.getActive(),
                formBo.getCreatedBy(),
                formBo.getCreatedOn(),
                formBo.getModifiedBy(),
                formBo.getModifiedOn());
      }
      formBoInsertQueryList.add(formInsertQuery);
    }
    insertSqlStatements.addAll(formBoInsertQueryList);
  }

  private void addComprehensionTestResponseBoListInsertSql(
      List<ComprehensionTestResponseBo> comprehensionTestResponseBoList,
      List<String> insertSqlStatements)
      throws SQLException {
    if (CollectionUtils.isEmpty(comprehensionTestResponseBoList)) {
      return;
    }

    List<String> comprehensionTestResponseBoInserQueryList = new ArrayList<>();
    String comprehensionTestResponseInsertQuery = null;
    for (ComprehensionTestResponseBo comprehensionTestResponseBo :
        comprehensionTestResponseBoList) {
      comprehensionTestResponseInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.COMREHENSION_TEST_RESPONSE,
              IdGenerator.id(),
              comprehensionTestResponseBo.getComprehensionTestQuestionId(),
              comprehensionTestResponseBo.getCorrectAnswer(),
              comprehensionTestResponseBo.getResponseOption());

      comprehensionTestResponseBoInserQueryList.add(comprehensionTestResponseInsertQuery);
    }
    insertSqlStatements.addAll(comprehensionTestResponseBoInserQueryList);
  }

  /*
   * private String createFileFromListAndSaveToCloudStorage(List<String> insertSqlStatements,
   * StudyBo studyBo) { try {
   *
   * Map<String, String> map = FdahpStudyDesignerUtil.getAppProperties(); String studyVersion =
   * map.get("studyVersion");
   *
   * FileWriter writer = new FileWriter(studyBo.getCustomStudyId() + "_" + studyVersion + ".sql");
   * for (String insertSqlStatement : insertSqlStatements) { writer.write(insertSqlStatement +
   * System.lineSeparator()); }
   *
   *
   * } catch (IOException e) { logger.error(String.format("create file failed due to %s",
   * e.getMessage()), e); }
   *
   * return null; }
   */

  public String saveFile(StudyBo studyBo, List<String> insertSqlStatements) throws IOException {
    String content = insertSqlStatements.toString();
    Map<String, String> map = FdahpStudyDesignerUtil.getAppProperties();
    String studyVersion = map.get("studyVersion");
    String fileName = studyBo.getCustomStudyId() + "_" + studyVersion + ".sql";
    String underDirectory = "InsertSqlQueries";
    String absoluteFileName = underDirectory + PATH_SEPARATOR + fileName;

    try {
      /*
       * fileWriter = new FileWriter(fileName); for (String insertSqlStatement :
       * insertSqlStatements) { fileWriter.write(insertSqlStatement + System.lineSeparator()); }
       */
      Storage storage = StorageOptions.getDefaultInstance().getService();
      BlobInfo blobInfo =
          BlobInfo.newBuilder(map.get("cloud.bucket.name"), absoluteFileName).build();
      storage.create(blobInfo, content.getBytes());

    } catch (Exception e) {
      logger.error("Save file to cloud storage failed", e);
    }

    /*
     * bytes = content.getBytes(); Storage storage =
     * StorageOptions.getDefaultInstance().getService(); try (WriteChannel writer =
     * storage.writer(blobInfo)) {
     *
     * writer.write(ByteBuffer.wrap(bytes, 0, bytes.length)); } catch (IOException e) {
     * logger.error("Save file in cloud storage failed", e); } }
     */
    return absoluteFileName;
  }

  private void addActiveTaskFrequencyBoInsertSqlQuery(
      List<ActiveTaskFrequencyBo> activeTaskFrequencyBoList,
      List<String> insertSqlStatements,
      List<String> newActiveTaskIds)
      throws SQLException {
    if (CollectionUtils.isEmpty(activeTaskFrequencyBoList)) {
      return;
    }

    List<String> activeTaskBoInsertQueryList = new ArrayList<>();
    String activeTaskBoInsertQuery = null;
    for (ActiveTaskFrequencyBo activeTaskFrquencyBo : activeTaskFrequencyBoList) {
      for (String newActiveTaskId : newActiveTaskIds) {
        activeTaskBoInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.ACTIVETASK_FREQUENCIES,
                IdGenerator.id(),
                newActiveTaskId,
                activeTaskFrquencyBo.getFrequencyDate(),
                activeTaskFrquencyBo.getFrequencyTime(),
                activeTaskFrquencyBo.getIsLaunchStudy(),
                activeTaskFrquencyBo.getIsStudyLifeTime(),
                activeTaskFrquencyBo.getTimePeriodFromDays(),
                activeTaskFrquencyBo.getTimePeriodToDays(),
                activeTaskFrquencyBo.isxDaysSign(),
                activeTaskFrquencyBo.isyDaysSign());
      }

      activeTaskBoInsertQueryList.add(activeTaskBoInsertQuery);
    }
    insertSqlStatements.addAll(activeTaskBoInsertQueryList);
  }

  private void addActiveTaskCustomScheduleBoInsertSqlQuery(
      List<ActiveTaskCustomScheduleBo> activeTaskCustomScheduleBoList,
      List<String> insertSqlStatements,
      List<String> newActiveTaskIds)
      throws SQLException {

    if (CollectionUtils.isEmpty(activeTaskCustomScheduleBoList)) {
      return;
    }

    List<String> activeTaskCustomScheduleBoInsertQueryList = new ArrayList<>();
    String activeTaskCustomScheduleBoInsertQuery = null;
    for (ActiveTaskCustomScheduleBo activeTaskCustomScheduleBo : activeTaskCustomScheduleBoList) {
      for (String newActiveTaskId : newActiveTaskIds) {
        activeTaskCustomScheduleBoInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.ACTIVETASK_CUSTOM_FREQUENCIES,
                IdGenerator.id(),
                newActiveTaskId,
                activeTaskCustomScheduleBo.getFrequencyEndDate(),
                activeTaskCustomScheduleBo.getFrequencyStartDate(),
                activeTaskCustomScheduleBo.getFrequencyTime(),
                activeTaskCustomScheduleBo.getTimePeriodFromDays(),
                activeTaskCustomScheduleBo.getTimePeriodToDays(),
                activeTaskCustomScheduleBo.isUsed(),
                activeTaskCustomScheduleBo.isxDaysSign(),
                activeTaskCustomScheduleBo.isyDaysSign());
      }

      activeTaskCustomScheduleBoInsertQueryList.add(activeTaskCustomScheduleBoInsertQuery);
    }
    insertSqlStatements.addAll(activeTaskCustomScheduleBoInsertQueryList);
  }

  private void addQuestionsResponseTypeInsertSql(
      List<QuestionReponseTypeBo> questionResponseTypeBoList,
      List<String> insertSqlStatements,
      List<String> questionResponseSubTypeIds)
      throws SQLException {

    if (CollectionUtils.isEmpty(questionResponseTypeBoList)) {
      return;
    }

    String questionResponseTypeBoInsertQuery = null;
    List<String> questionResponseTypeBoInsertQueryList = new ArrayList<>();
    for (QuestionReponseTypeBo questionResponseTypeBo : questionResponseTypeBoList) {
      for (String questionResponseSubTypeId : questionResponseSubTypeIds) {
        questionResponseTypeBoInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.RESPONSE_TYPE_VALUE,
                questionResponseSubTypeId,
                questionResponseTypeBo.getActive(),
                questionResponseTypeBo.getConditionFormula(),
                questionResponseTypeBo.getDefaultDate(),
                questionResponseTypeBo.getDefaultDate(),
                questionResponseTypeBo.getDefaultTime(),
                questionResponseTypeBo.getDefaultValue(),
                questionResponseTypeBo.getFormulaBasedLogic(),
                questionResponseTypeBo.getImageSize(),
                questionResponseTypeBo.getInvalidMessage(),
                questionResponseTypeBo.getMaxDate(),
                questionResponseTypeBo.getMaxDescription(),
                questionResponseTypeBo.getMaxFractionDigits(),
                questionResponseTypeBo.getMaxImage(),
                questionResponseTypeBo.getMaxLength(),
                questionResponseTypeBo.getMaxValue(),
                questionResponseTypeBo.getMeasurementSystem(),
                questionResponseTypeBo.getMinDate(),
                questionResponseTypeBo.getMinDescription(),
                questionResponseTypeBo.getMinImage(),
                questionResponseTypeBo.getMinValue(),
                questionResponseTypeBo.getMultipleLines(),
                questionResponseTypeBo.getOtherDescription(),
                questionResponseTypeBo.getOtherDestinationStepId(),
                questionResponseTypeBo.getOtherExclusive(),
                questionResponseTypeBo.getOtherIncludeText(),
                questionResponseTypeBo.getOtherParticipantFill(),
                questionResponseTypeBo.getOtherPlaceholderText(),
                questionResponseTypeBo.getOtherText(),
                questionResponseTypeBo.getOtherType(),
                questionResponseTypeBo.getOtherValue(),
                questionResponseTypeBo.getPlaceholder(),
                questionResponseTypeBo.getQuestionsResponseTypeId(),
                questionResponseTypeBo.getSelectionStyle(),
                questionResponseTypeBo.getStep(),
                questionResponseTypeBo.getStyle(),
                questionResponseTypeBo.getTextChoices(),
                questionResponseTypeBo.getUnit(),
                questionResponseTypeBo.getUseCurrentLocation(),
                questionResponseTypeBo.getValidationCharacters(),
                questionResponseTypeBo.getValidationCondition(),
                questionResponseTypeBo.getValidationExceptText(),
                questionResponseTypeBo.getValidationRegex(),
                questionResponseTypeBo.getVertical());
      }
      questionResponseTypeBoInsertQueryList.add(questionResponseTypeBoInsertQuery);
    }
    insertSqlStatements.addAll(questionResponseTypeBoInsertQueryList);
  }

  private void addQuestionsResponseSubTypeInsertSql(
      List<QuestionResponseSubTypeBo> questionResponseSubTypeBoList,
      List<String> insertSqlStatements,
      List<String> questionResponseSubTypeIds)
      throws SQLException {

    if (CollectionUtils.isEmpty(questionResponseSubTypeBoList)) {
      return;
    }

    String questionResponseSubTypeBoInsertQuery = null;
    List<String> questionResponseSubTypeBoInsertQueryList = new ArrayList<>();
    for (QuestionResponseSubTypeBo questionResponseSubTypeBo : questionResponseSubTypeBoList) {
      for (String questionResponseSubTypeId : questionResponseSubTypeIds) {
        questionResponseSubTypeBoInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.RESPONSE_SUB_TYPE_VALUE,
                questionResponseSubTypeId,
                questionResponseSubTypeBo.getActive(),
                questionResponseSubTypeBo.getDescription(),
                questionResponseSubTypeBo.getDestinationStepId(),
                questionResponseSubTypeBo.getDetail(),
                questionResponseSubTypeBo.getExclusive(),
                questionResponseSubTypeBo.getImage(),
                questionResponseSubTypeBo.getResponseTypeId(),
                questionResponseSubTypeBo.getSelectedImage(),
                questionResponseSubTypeBo.getText(),
                questionResponseSubTypeBo.getValue());
      }
      questionResponseSubTypeBoInsertQueryList.add(questionResponseSubTypeBoInsertQuery);
    }
    insertSqlStatements.addAll(questionResponseSubTypeBoInsertQueryList);
  }

  private void addInstructionInsertSql(
      List<InstructionsBo> instructionList,
      List<String> insertSqlStatements,
      List<String> instructionIds)
      throws SQLException {
    List<String> instructionBoInsertQueryList = new ArrayList<>();
    if (CollectionUtils.isEmpty(instructionList)) {
      return;
    }

    String instructionBoInsertQuery = null;
    for (InstructionsBo instructionBo : instructionList) {
      for (String instructionId : instructionIds) {
        instructionBoInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.INSTRUCTION,
                instructionId,
                instructionBo.getActive(),
                instructionBo.getCreatedBy(),
                instructionBo.getCreatedOn(),
                instructionBo.getInstructionText(),
                instructionBo.getInstructionTitle(),
                instructionBo.getModifiedBy(),
                instructionBo.getModifiedOn(),
                instructionBo.getStatus());
      }
      instructionBoInsertQueryList.add(instructionBoInsertQuery);
    }
    insertSqlStatements.addAll(instructionBoInsertQueryList);
  }

  private void addFormMappingListInsertSql(
      List<FormMappingBo> formsList, List<String> insertSqlStatements, List<String> formIds)
      throws SQLException {

    List<String> formMappingBoInsertQueryList = new ArrayList<>();
    if (CollectionUtils.isEmpty(formsList)) {
      return;
    }

    String formMappingInsertQuery = null;
    for (FormMappingBo formMappingBo : formsList) {
      for (String formId : formIds) {
        formMappingInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.FORM_MAPPING,
                formId,
                formMappingBo.getActive(),
                formMappingBo.getFormId(),
                formMappingBo.getQuestionId(),
                formMappingBo.getSequenceNo());
      }
      formMappingBoInsertQueryList.add(formMappingInsertQuery);
    }
    insertSqlStatements.addAll(formMappingBoInsertQueryList);
  }

  private void addQuestionListInsertSql(
      List<QuestionsBo> questionsList, List<String> insertSqlStatements, List<String> questionIds)
      throws SQLException {

    List<String> questionsBoInsertQueryList = new ArrayList<>();
    if (CollectionUtils.isEmpty(questionsList)) {
      return;
    }

    String questionInsertQuery = null;
    for (QuestionsBo questionBo : questionsList) {
      for (String questionId : questionIds) {
        questionInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.QUESTIONS,
                questionId,
                questionBo.getActive(),
                questionBo.getAddLineChart(),
                questionBo.getAllowHealthKit(),
                questionBo.getAllowRollbackChart(),
                questionBo.getAnchorDateId(),
                questionBo.getChartTitle(),
                questionBo.getCreatedBy(),
                questionBo.getCreatedOn(),
                questionBo.getDescription(),
                questionBo.getHealthkitDatatype(),
                questionBo.getLineChartTimeRange(),
                questionBo.getModifiedBy(),
                questionBo.getModifiedOn(),
                questionBo.getQuestion(),
                questionBo.getResponseType(),
                questionBo.getShortTitle(),
                questionBo.getSkippable(),
                questionBo.getStatDisplayName(),
                questionBo.getStatDisplayUnits(),
                questionBo.getStatFormula(),
                questionBo.getStatShortName(),
                questionBo.getStatType(),
                questionBo.getStatus(),
                questionBo.getUseAnchorDate(),
                questionBo.getUseStasticData());
      }
      questionsBoInsertQueryList.add(questionInsertQuery);
    }
    insertSqlStatements.addAll(questionsBoInsertQueryList);
  }

  private void addQuestionnaireCustomScheduleBoInsertSql(
      List<QuestionnaireCustomScheduleBo> questionnairesCustomFrequenciesBoList,
      List<String> insertSqlStatements,
      List<String> newQuestionnaireIds)
      throws SQLException {

    List<String> questionnairesCustomScheduleBoInsertQueryList = new ArrayList<>();
    if (CollectionUtils.isEmpty(questionnairesCustomFrequenciesBoList)) {
      return;
    }

    String questionnairesCustomScheduleBoInsertQuery = null;
    for (QuestionnaireCustomScheduleBo questionnaireCustomScheduleBo :
        questionnairesCustomFrequenciesBoList) {
      for (String newQuestionnaireId : newQuestionnaireIds) {
        questionnairesCustomScheduleBoInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.QUESTIONNAIRES_CUSTOM_FREQUENCIES,
                IdGenerator.id(),
                questionnaireCustomScheduleBo.getFrequencyEndDate(),
                questionnaireCustomScheduleBo.getFrequencyStartDate(),
                questionnaireCustomScheduleBo.getFrequencyTime(),
                newQuestionnaireId,
                questionnaireCustomScheduleBo.getTimePeriodFromDays(),
                questionnaireCustomScheduleBo.getTimePeriodToDays(),
                questionnaireCustomScheduleBo.isUsed(),
                questionnaireCustomScheduleBo.isxDaysSign(),
                questionnaireCustomScheduleBo.isyDaysSign());
      }
      questionnairesCustomScheduleBoInsertQueryList.add(questionnairesCustomScheduleBoInsertQuery);
    }
    insertSqlStatements.addAll(questionnairesCustomScheduleBoInsertQueryList);
  }

  private void addQuestionnairesStepsListInsertSql(
      List<QuestionnairesStepsBo> questionnairesStepsList,
      List<String> insertSqlStatements,
      List<String> newQuestionnaireIds,
      List<String> questionFormInstructionIds)
      throws SQLException {
    if (CollectionUtils.isEmpty(questionnairesStepsList)) {
      return;
    }
    String questionnaireStepsBoInsertQuery = null;
    List<String> questionnaireStepsBoInsertQueryList = new ArrayList<>();
    for (QuestionnairesStepsBo questionnairesStepsBo : questionnairesStepsList) {
      for (String newQuestionnaireId : newQuestionnaireIds) {
        for (String questionFormInstructionId : questionFormInstructionIds) {
          questionnaireStepsBoInsertQuery =
              prepareInsertQuery(
                  StudyExportSqlQueries.QUESTIONNAIRES_STEPS,
                  IdGenerator.id(),
                  questionnairesStepsBo.getActive(),
                  questionnairesStepsBo.getCreatedBy(),
                  questionnairesStepsBo.getCreatedOn(),
                  questionnairesStepsBo.getDestinationStep(),
                  questionFormInstructionId,
                  questionnairesStepsBo.getModifiedBy(),
                  questionnairesStepsBo.getModifiedOn(),
                  newQuestionnaireId,
                  questionnairesStepsBo.getRepeatable(),
                  questionnairesStepsBo.getRepeatableText(),
                  questionnairesStepsBo.getSequenceNo(),
                  questionnairesStepsBo.getSkiappable(),
                  questionnairesStepsBo.getStatus(),
                  questionnairesStepsBo.getStepShortTitle(),
                  questionnairesStepsBo.getStepType());
        }
      }

      questionnaireStepsBoInsertQueryList.add(questionnaireStepsBoInsertQuery);
    }
    insertSqlStatements.addAll(questionnaireStepsBoInsertQueryList);
  }

  private void addStudiesInsertSql(
      StudyBo studyBo, List<String> insertSqlStatements, String newStudyId, String newCustomId)
      throws SQLException {

    if (studyBo == null) {
      return;
    }

    String studiesInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.STUDIES,
            newStudyId,
            studyBo.getAppId(),
            studyBo.getCategory(),
            studyBo.getCreatedBy(),
            studyBo.getCreatedOn(),
            newCustomId,
            studyBo.getDescription(),
            studyBo.getEnrollingParticipants(),
            studyBo.getFullName(),
            studyBo.getHasActivetaskDraft(),
            studyBo.getHasActivityDraft(),
            studyBo.getHasConsentDraft(),
            studyBo.getHasQuestionnaireDraft(),
            studyBo.getHasStudyDraft(),
            studyBo.getInboxEmailAddress(),
            studyBo.getIrbReview(),
            studyBo.getLive(),
            studyBo.getMediaLink(),
            studyBo.getModifiedBy(),
            studyBo.getModifiedOn(),
            studyBo.getName(),
            studyBo.getPlatform(),
            studyBo.getResearchSponsor(),
            studyBo.getSequenceNumber(),
            studyBo.getStatus(),
            studyBo.isStudyPreActiveFlag(),
            studyBo.getStudyTagLine(),
            studyBo.getStudyWebsite(),
            studyBo.getStudylunchDate(),
            studyBo.getTentativeDuration(),
            studyBo.getTentativeDurationWeekmonth(),
            studyBo.getThumbnailImage(),
            studyBo.getType(),
            studyBo.getVersion());

    insertSqlStatements.add(studiesInsertQuery);
  }

  private void addStudySequenceInsertSql(
      StudySequenceBo studySequenceBo, List<String> insertSqlStatements, String newStudyId)
      throws SQLException {

    if (studySequenceBo == null) {
      return;
    }

    String studySequeneInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.STUDY_SEQUENCE,
            IdGenerator.id(),
            studySequenceBo.isActions(),
            studySequenceBo.isBasicInfo(),
            studySequenceBo.isCheckList(),
            studySequenceBo.isComprehensionTest(),
            studySequenceBo.isConsentEduInfo(),
            studySequenceBo.iseConsent(),
            studySequenceBo.isEligibility(),
            studySequenceBo.isMiscellaneousBranding(),
            studySequenceBo.isMiscellaneousNotification(),
            studySequenceBo.isMiscellaneousResources(),
            studySequenceBo.isOverView(),
            studySequenceBo.isSettingAdmins(),
            studySequenceBo.isStudyDashboardChart(),
            studySequenceBo.isStudyDashboardStats(),
            studySequenceBo.isStudyExcActiveTask(),
            studySequenceBo.isStudyExcQuestionnaries(),
            newStudyId);
    insertSqlStatements.add(studySequeneInsertQuery);
  }

  private void addAnchorDateInsertSql(
      AnchorDateTypeBo anchorDate,
      List<String> insertSqlStatements,
      String newStudyId,
      String newCustomId)
      throws SQLException {

    if (anchorDate == null) {
      return;
    }

    String anchorDateTypeInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.ANCHORDATE_TYPE,
            IdGenerator.id(),
            newCustomId,
            anchorDate.getHasAnchortypeDraft(),
            anchorDate.getName(),
            newStudyId,
            anchorDate.getVersion());

    insertSqlStatements.add(anchorDateTypeInsertQuery);
  }

  private void addStudypagesListInsertSql(
      List<StudyPageBo> studypageList, List<String> insertSqlStatements, String newStudyId)
      throws SQLException {

    if (CollectionUtils.isEmpty(studypageList)) {
      return;
    }

    List<String> studyPageBoInsertQueryList = new ArrayList<>();
    for (StudyPageBo studyPageBo : studypageList) {
      String studyPageBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.STUDY_PAGE,
              IdGenerator.id(),
              studyPageBo.getCreatedBy(),
              studyPageBo.getCreatedOn(),
              studyPageBo.getDescription(),
              studyPageBo.getImagePath(),
              studyPageBo.getModifiedBy(),
              studyPageBo.getModifiedOn(),
              newStudyId,
              studyPageBo.getTitle());

      studyPageBoInsertQueryList.add(studyPageBoInsertQuery);
    }
    insertSqlStatements.addAll(studyPageBoInsertQueryList);
  }

  private void addEligibilityInsertSql(
      EligibilityBo eligibilityBo,
      List<String> insertSqlStatements,
      String newEligibilityId,
      String newStudyId)
      throws SQLException {

    if (eligibilityBo == null) {
      return;
    }

    String eligibilityInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.ELIGIBILITY,
            newEligibilityId,
            eligibilityBo.getCreatedBy(),
            eligibilityBo.getCreatedOn(),
            eligibilityBo.getEligibilityMechanism(),
            eligibilityBo.getFailureOutcomeText(),
            eligibilityBo.getInstructionalText(),
            eligibilityBo.getModifiedBy(),
            eligibilityBo.getModifiedOn(),
            newStudyId);

    insertSqlStatements.add(eligibilityInsertQuery);
  }

  private void addNotificationInsertSql(
      List<NotificationBO> notificationBOs,
      List<String> insertSqlStatements,
      String newStudyId,
      String newCustomId,
      List<String> newQuestionnaireIds,
      List<String> newActiveTaskIds)
      throws SQLException {

    if (CollectionUtils.isEmpty(notificationBOs)) {
      return;
    }
    List<String> notificationBoBoInsertQueryList = new ArrayList<>();
    String notificationBoInsertQuery = null;
    for (NotificationBO notificationBO : notificationBOs) {
      for (String newQuestionnaireId : newQuestionnaireIds) {
        for (String newActiveTaskId : newActiveTaskIds) {
          notificationBoInsertQuery =
              prepareInsertQuery(
                  StudyExportSqlQueries.NOTIFICATION,
                  IdGenerator.id(),
                  newActiveTaskId,
                  notificationBO.isAnchorDate(),
                  notificationBO.getAppId(),
                  notificationBO.getCreatedBy(),
                  notificationBO.getCreatedOn(),
                  newCustomId,
                  notificationBO.getModifiedBy(),
                  notificationBO.getModifiedOn(),
                  notificationBO.isNotificationAction(),
                  notificationBO.isNotificationDone(),
                  notificationBO.getNotificationScheduleType(),
                  notificationBO.isNotificationSent(),
                  notificationBO.isNotificationStatus(),
                  notificationBO.getNotificationSubType(),
                  notificationBO.getNotificationText(),
                  notificationBO.getNotificationType(),
                  newQuestionnaireId,
                  notificationBO.getResourceId(),
                  notificationBO.getScheduleDate(),
                  notificationBO.getScheduleTime(),
                  newStudyId,
                  notificationBO.getxDays(),
                  notificationBO.getScheduleTimestamp());
        }
      }

      notificationBoBoInsertQueryList.add(notificationBoInsertQuery);
    }
    insertSqlStatements.addAll(notificationBoBoInsertQueryList);
  }

  private void addStudyActiveTaskInsertSql(
      List<ActiveTaskBo> activeTaskBos,
      List<String> insertSqlStatements,
      List<String> newActiveTaskIds,
      String newStudyId,
      String newCustomId)
      throws SQLException {

    if (CollectionUtils.isEmpty(activeTaskBos)) {
      return;
    }
    List<String> activeTaskBoInsertQueryList = new ArrayList<>();
    String activeTaskBoInsertQuery = null;
    for (ActiveTaskBo activeTaskBo : activeTaskBos) {
      for (String newActiveTaskId : newActiveTaskIds) {
        activeTaskBoInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.ACTIVETASK_SQL,
                newActiveTaskId,
                activeTaskBo.isAction(),
                activeTaskBo.getActive(),
                activeTaskBo.getActiveTaskLifetimeEnd(),
                activeTaskBo.getActiveTaskLifetimeStart(),
                activeTaskBo.getAnchorDateId(),
                activeTaskBo.getCreatedBy(),
                activeTaskBo.getCreatedDate(),
                newCustomId,
                activeTaskBo.getDayOfTheWeek(),
                activeTaskBo.getDisplayName(),
                activeTaskBo.getDuration(),
                activeTaskBo.getFrequency(),
                activeTaskBo.getInstruction(),
                activeTaskBo.getIsChange(),
                activeTaskBo.getLive(),
                activeTaskBo.getModifiedBy(),
                activeTaskBo.getModifiedDate(),
                activeTaskBo.getRepeatActiveTask(),
                activeTaskBo.getScheduleType(),
                activeTaskBo.getShortTitle(),
                newStudyId,
                activeTaskBo.getTaskTypeId(),
                activeTaskBo.getTitle(),
                activeTaskBo.getVersion());
      }

      activeTaskBoInsertQueryList.add(activeTaskBoInsertQuery);
    }
    insertSqlStatements.addAll(activeTaskBoInsertQueryList);
  }

  private void addActiveTaskAtrributeValuesInsertSql(
      List<ActiveTaskAtrributeValuesBo> activeTaskAttributeBos,
      List<String> insertSqlStatements,
      List<String> newActiveTaskIds)
      throws SQLException {

    if (CollectionUtils.isEmpty(activeTaskAttributeBos)) {
      return;
    }
    List<String> activeTaskAtrributeInsertQueryList = new ArrayList<>();
    String activeTaskAtrributeInsertQuery = null;
    for (ActiveTaskAtrributeValuesBo activeTaskAtrributeValuesBo : activeTaskAttributeBos) {
      for (String newActiveTaskId : newActiveTaskIds) {
        activeTaskAtrributeInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.ACTIVETASK_ATTRIBUTES_VALUES,
                IdGenerator.id(),
                activeTaskAtrributeValuesBo.getActive(),
                newActiveTaskId,
                activeTaskAtrributeValuesBo.getActiveTaskMasterAttrId(),
                activeTaskAtrributeValuesBo.isAddToLineChart(),
                activeTaskAtrributeValuesBo.getAttributeVal(),
                activeTaskAtrributeValuesBo.getDisplayNameStat(),
                activeTaskAtrributeValuesBo.getDisplayUnitStat(),
                activeTaskAtrributeValuesBo.getFormulaAppliedStat(),
                activeTaskAtrributeValuesBo.getIdentifierNameStat(),
                activeTaskAtrributeValuesBo.getRollbackChat(),
                activeTaskAtrributeValuesBo.getTimeRangeChart(),
                activeTaskAtrributeValuesBo.getTimeRangeStat(),
                activeTaskAtrributeValuesBo.getTitleChat(),
                activeTaskAtrributeValuesBo.getUploadTypeStat(),
                activeTaskAtrributeValuesBo.isUseForStatistic());
      }

      activeTaskAtrributeInsertQueryList.add(activeTaskAtrributeInsertQuery);
    }
    insertSqlStatements.addAll(activeTaskAtrributeInsertQueryList);
  }

  private void addResourceInsertSql(
      List<ResourceBO> resourceBOs, List<String> insertSqlStatements, String newStudyId)
      throws SQLException {

    if (CollectionUtils.isEmpty(resourceBOs)) {
      return;
    }
    List<String> resourceBoInsertQueryList = new ArrayList<>();
    for (ResourceBO resourceBO : resourceBOs) {
      String resourceBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.RESOURCES,
              resourceBO.getId(),
              resourceBO.isAction(),
              resourceBO.getAnchorDateId(),
              resourceBO.getCreatedBy(),
              resourceBO.getCreatedOn(),
              resourceBO.getEndDate(),
              resourceBO.getModifiedBy(),
              resourceBO.getModifiedOn(),
              resourceBO.getPdfName(),
              resourceBO.getPdfUrl(),
              resourceBO.getResourceText(),
              resourceBO.isResourceType(),
              resourceBO.isResourceVisibility(),
              resourceBO.getRichText(),
              resourceBO.getSequenceNo(),
              resourceBO.getStartDate(),
              resourceBO.isStatus(),
              resourceBO.getStudyId(),
              resourceBO.isStudyProtocol(),
              resourceBO.isTextOrPdf(),
              resourceBO.getTimePeriodFromDays(),
              resourceBO.getTimePeriodToDays(),
              resourceBO.getTitle(),
              resourceBO.isxDaysSign(),
              resourceBO.isyDaysSign());

      resourceBoInsertQueryList.add(resourceBoInsertQuery);
    }
    insertSqlStatements.addAll(resourceBoInsertQueryList);
  }

  private void addStudyPermissionInsertQuery(
      StudyPermissionBO studyPermissionBo, List<String> insertSqlStatements, String newStudyId)
      throws SQLException {

    if (studyPermissionBo == null) {
      return;
    }

    String studyPermissionsInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.STUDY_PERMISSION,
            getNewId(),
            studyPermissionBo.getDelFlag(),
            studyPermissionBo.getProjectLead(),
            newStudyId,
            studyPermissionBo.getUserId(),
            studyPermissionBo.isViewPermission());

    insertSqlStatements.add(studyPermissionsInsertQuery);
  }

  private void addEligibilityTestListInsertSql(
      List<EligibilityTestBo> eligibilityTestBoList,
      List<String> insertSqlStatements,
      String newEligibilityId)
      throws SQLException {
    if (CollectionUtils.isEmpty(eligibilityTestBoList)) {
      return;
    }
    List<String> eligibilityTestBoInsertQueryList = new ArrayList<>();
    for (EligibilityTestBo eligibilityTestBo : eligibilityTestBoList) {

      String eligibilityTestBoBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.ELIGIBILITY_TEST,
              getNewId(),
              eligibilityTestBo.getActive(),
              newEligibilityId,
              eligibilityTestBo.getQuestion(),
              eligibilityTestBo.getResponseFormat(),
              eligibilityTestBo.getResponseNoOption(),
              eligibilityTestBo.getResponseYesOption(),
              eligibilityTestBo.getSequenceNo(),
              eligibilityTestBo.getShortTitle(),
              eligibilityTestBo.getStatus(),
              eligibilityTestBo.isUsed());

      eligibilityTestBoInsertQueryList.add(eligibilityTestBoBoInsertQuery);
    }
    insertSqlStatements.addAll(eligibilityTestBoInsertQueryList);
  }

  private void addConsentBoListInsertSql(
      List<ConsentBo> consentBoList,
      List<String> insertSqlStatements,
      String newStudyId,
      String newCustomId)
      throws SQLException {

    if (CollectionUtils.isEmpty(consentBoList)) {
      return;
    }

    List<String> consentBoListInsertQuery = new ArrayList<>();
    for (ConsentBo consentBo : consentBoList) {
      String consentInsertSql =
          prepareInsertQuery(
              StudyExportSqlQueries.CONSENT,
              IdGenerator.id(),
              consentBo.getAllowWithoutPermission(),
              consentBo.getComprehensionTestMinimumScore(),
              consentBo.getConsentDocContent(),
              consentBo.getConsentDocType(),
              consentBo.getCreatedBy(),
              consentBo.getCreatedOn(),
              newCustomId,
              consentBo.geteConsentAgree(),
              consentBo.geteConsentDatetime(),
              consentBo.geteConsentFirstName(),
              consentBo.geteConsentLastName(),
              consentBo.geteConsentSignature(),
              consentBo.getHtmlConsent(),
              consentBo.getLearnMoreText(),
              consentBo.getLive(),
              consentBo.getLongDescription(),
              consentBo.getModifiedBy(),
              consentBo.getModifiedOn(),
              consentBo.getNeedComprehensionTest(),
              consentBo.getShareDataPermissions(),
              consentBo.getShortDescription(),
              newStudyId,
              consentBo.getTaglineDescription(),
              consentBo.getTitle(),
              consentBo.getVersion(),
              consentBo.getEnrollAgain());
      consentBoListInsertQuery.add(consentInsertSql);
    }
    insertSqlStatements.addAll(consentBoListInsertQuery);
  }

  private void addConsentInfoBoListInsertSql(
      List<ConsentInfoBo> consentInfoBoList,
      List<String> insertSqlStatements,
      String newStudyId,
      String newCustomId)
      throws SQLException {

    if (CollectionUtils.isEmpty(consentInfoBoList)) {
      return;
    }

    List<String> consentInfoInsertQueryList = new ArrayList<>();
    for (ConsentInfoBo consentInfoBo : consentInfoBoList) {
      String consentInfoInsertSql =
          prepareInsertQuery(
              StudyExportSqlQueries.CONSENT_INFO,
              IdGenerator.id(),
              consentInfoBo.getActive(),
              consentInfoBo.getBriefSummary(),
              consentInfoBo.getConsentItemTitleId(),
              consentInfoBo.getConsentItemType(),
              consentInfoBo.getContentType(),
              consentInfoBo.getCreatedBy(),
              consentInfoBo.getCreatedOn(),
              newCustomId,
              consentInfoBo.getDisplayTitle(),
              consentInfoBo.getElaborated(),
              consentInfoBo.getHtmlContent(),
              consentInfoBo.getLive(),
              consentInfoBo.getModifiedBy(),
              consentInfoBo.getModifiedOn(),
              consentInfoBo.getSequenceNo(),
              consentInfoBo.getStatus(),
              newStudyId,
              consentInfoBo.getUrl(),
              consentInfoBo.getVersion(),
              consentInfoBo.getVisualStep());

      consentInfoInsertQueryList.add(consentInfoInsertSql);
    }
    insertSqlStatements.addAll(consentInfoInsertQueryList);
  }

  private void addComprehensionTestQuestionListInsertSql(
      List<ComprehensionTestQuestionBo> comprehensionTestQuestionList,
      List<String> insertSqlStatements,
      String newStudyId)
      throws SQLException {

    if (CollectionUtils.isEmpty(comprehensionTestQuestionList)) {
      return;
    }

    List<String> comprehensionTestQuestionInsertQueryList = new ArrayList<>();
    for (ComprehensionTestQuestionBo comprehensionTestQuestionBo : comprehensionTestQuestionList) {
      String comprehensionTestQuestionInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.COMREHENSION_TEST_QUESTIONS,
              IdGenerator.id(),
              comprehensionTestQuestionBo.getActive(),
              comprehensionTestQuestionBo.getCreatedBy(),
              comprehensionTestQuestionBo.getCreatedOn(),
              comprehensionTestQuestionBo.getModifiedBy(),
              comprehensionTestQuestionBo.getModifiedOn(),
              comprehensionTestQuestionBo.getQuestionText(),
              comprehensionTestQuestionBo.getSequenceNo(),
              comprehensionTestQuestionBo.getStatus(),
              comprehensionTestQuestionBo.getStructureOfCorrectAns(),
              newStudyId);

      comprehensionTestQuestionInsertQueryList.add(comprehensionTestQuestionInsertQuery);
    }
    insertSqlStatements.addAll(comprehensionTestQuestionInsertQueryList);
  }

  private void addQuestionnaireBoListInsertSql(
      List<QuestionnaireBo> questionnairesList,
      List<String> insertSqlStatements,
      String newStudyId,
      String newCustomId,
      List<String> newQuestionnaireIds)
      throws SQLException {

    if (CollectionUtils.isEmpty(questionnairesList)) {
      return;
    }

    List<String> questionnairesBoInsertQueryList = new ArrayList<>();
    for (QuestionnaireBo questionnaireBo : questionnairesList) {
      String questionnairesBoInsertQuery = null;
      for (String questionaireId : newQuestionnaireIds) {
        questionnairesBoInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.QUESTIONNAIRES,
                questionaireId,
                questionnaireBo.getActive(),
                questionnaireBo.getAnchorDateId(),
                questionnaireBo.getBranching(),
                questionnaireBo.getCreatedBy(),
                questionnaireBo.getCreatedDate(),
                newCustomId,
                questionnaireBo.getDayOfTheWeek(),
                questionnaireBo.getFrequency(),
                questionnaireBo.getIsChange(),
                questionnaireBo.getLive(),
                questionnaireBo.getModifiedBy(),
                questionnaireBo.getModifiedDate(),
                questionnaireBo.getRepeatQuestionnaire(),
                questionnaireBo.getScheduleType(),
                questionnaireBo.getShortTitle(),
                questionnaireBo.getStatus(),
                newStudyId,
                questionnaireBo.getStudyLifetimeEnd(),
                questionnaireBo.getStudyLifetimeStart(),
                questionnaireBo.getTitle(),
                questionnaireBo.getVersion());
      }

      questionnairesBoInsertQueryList.add(questionnairesBoInsertQuery);
    }
    insertSqlStatements.addAll(questionnairesBoInsertQueryList);
  }

  private void addQuestionnaireFrequenciesBoInsertSql(
      List<QuestionnairesFrequenciesBo> questionnairesFrequenciesBoList,
      List<String> insertSqlStatements,
      List<String> newQuestionnaireIds)
      throws SQLException {

    List<String> questionnairesFrequenciesBoInsertQueryList = new ArrayList<>();
    if (CollectionUtils.isEmpty(questionnairesFrequenciesBoList)) {
      return;
    }
    String questionnairesFrequenciesBoInsertQuery = null;
    for (QuestionnairesFrequenciesBo questionnairesFrequenciesBo :
        questionnairesFrequenciesBoList) {
      for (String questtionnairesId : newQuestionnaireIds) {
        questionnairesFrequenciesBoInsertQuery =
            prepareInsertQuery(
                StudyExportSqlQueries.QUESTIONNAIRES_FREQUENCIES,
                IdGenerator.id(),
                questionnairesFrequenciesBo.getFrequencyDate(),
                questionnairesFrequenciesBo.getFrequencyTime(),
                questionnairesFrequenciesBo.getIsLaunchStudy(),
                questionnairesFrequenciesBo.getIsStudyLifeTime(),
                questtionnairesId,
                questionnairesFrequenciesBo.getTimePeriodFromDays(),
                questionnairesFrequenciesBo.getTimePeriodToDays(),
                questionnairesFrequenciesBo.isxDaysSign(),
                questionnairesFrequenciesBo.isyDaysSign());
      }
      questionnairesFrequenciesBoInsertQueryList.add(questionnairesFrequenciesBoInsertQuery);
    }

    insertSqlStatements.addAll(questionnairesFrequenciesBoInsertQueryList);
  }

  private String prepareInsertQuery(String sqlQuery, Object... values) throws SQLException {
    Object[] columns =
        sqlQuery
            .substring(sqlQuery.indexOf('(') + 1, sqlQuery.indexOf(")"))
            .replace("`", "")
            .split(",");

    if (columns.length != values.length) {
      throw new SQLException("Column count doesn't match value count.");
    }

    int i = 0;
    for (Object column : columns) {
      column = ((String) column).trim();
      if (values[i] instanceof String) {
        sqlQuery = sqlQuery.replace("<" + column + ">", "'" + values[i] + "'");
      } else {
        sqlQuery = sqlQuery.replace("<" + column + ">", "" + values[i] + "");
      }

      i++;
    }

    return sqlQuery;
  }

  private String getNewId() {
    return com.fdahpstudydesigner.util.IdGenerator.id();
  }

  private QuestionnairesStepsIdsBean getInstructionFormIds(
      List<QuestionsBo> questionsList,
      List<FormMappingBo> formMappingList,
      List<InstructionsBo> instructionList,
      List<FormBo> formsList,
      List<QuestionResponseSubTypeBo> questionResponseSubTypeBoList,
      List<QuestionReponseTypeBo> questionResponseTypeBo,
      List<String> questionFormInstructionIds) {

    QuestionnairesStepsIdsBean questionnairesStepsIdsBean = new QuestionnairesStepsIdsBean();
    if (CollectionUtils.isNotEmpty(questionsList)) {
      questionnairesStepsIdsBean.getQuestionIds().add(IdGenerator.id());
    }

    if (CollectionUtils.isNotEmpty(formMappingList)) {
      questionnairesStepsIdsBean.getFormMappingIds().add(IdGenerator.id());
    }

    if (CollectionUtils.isNotEmpty(instructionList)) {
      questionnairesStepsIdsBean.getInstructionIds().add(IdGenerator.id());
    }

    if (CollectionUtils.isNotEmpty(questionResponseSubTypeBoList)) {
      questionnairesStepsIdsBean.getQuestionResponseSubTypeIds().add(IdGenerator.id());
    }

    if (CollectionUtils.isNotEmpty(questionResponseTypeBo)) {
      questionnairesStepsIdsBean.getQuestionResponseIds().add(IdGenerator.id());
    }

    if (CollectionUtils.isNotEmpty(formsList)) {
      questionnairesStepsIdsBean.getFormsIds().add(IdGenerator.id());
    }

    questionFormInstructionIds.addAll(questionnairesStepsIdsBean.getQuestionIds());
    questionFormInstructionIds.addAll(questionnairesStepsIdsBean.getFormMappingIds());
    questionFormInstructionIds.addAll(questionnairesStepsIdsBean.getInstructionIds());
    questionFormInstructionIds.addAll(questionnairesStepsIdsBean.getQuestionResponseSubTypeIds());
    questionFormInstructionIds.addAll(questionnairesStepsIdsBean.getQuestionResponseIds());
    questionFormInstructionIds.addAll(questionnairesStepsIdsBean.getFormsIds());

    return questionnairesStepsIdsBean;
  }
}
