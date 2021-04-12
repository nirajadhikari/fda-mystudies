package com.fdahpstudydesigner.service;

import com.fdahpstudydesigner.bo.ActiveTaskAtrributeValuesBo;
import com.fdahpstudydesigner.bo.ActiveTaskBo;
import com.fdahpstudydesigner.bo.AnchorDateTypeBo;
import com.fdahpstudydesigner.bo.ComprehensionTestQuestionBo;
import com.fdahpstudydesigner.bo.ConsentBo;
import com.fdahpstudydesigner.bo.ConsentInfoBo;
import com.fdahpstudydesigner.bo.ConsentMasterInfoBo;
import com.fdahpstudydesigner.bo.EligibilityBo;
import com.fdahpstudydesigner.bo.EligibilityTestBo;
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
import com.fdahpstudydesigner.util.StudyExportSqlQueries;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

  public String exportStudy(String studyId, String userId) {

    List<String> insertSqlStatements = new ArrayList<>();

    StudyBo studyBo = studyDao.getStudy(studyId);

    StudyPermissionBO studyPermissionBo = studyDao.getStudyPermissionBO(studyBo.getId(), userId);

    StudySequenceBo studySequenceBo = studyDao.getStudySequenceByStudyId(studyBo.getId());
    AnchorDateTypeBo anchorDate = studyDao.getAnchorDateDetails(studyBo.getId());

    List<StudyPageBo> studypageList = studyDao.getOverviewStudyPagesById(studyBo.getId(), userId);

    EligibilityBo eligibilityBo = studyDao.getStudyEligibiltyByStudyId(studyBo.getId());

    List<EligibilityTestBo> eligibilityBoList = null;
    if (eligibilityBo != null) {
      eligibilityBoList = studyDao.viewEligibilityTestQusAnsByEligibilityId(eligibilityBo.getId());
    }

    List<ConsentBo> consentBoList = studyDao.getConsentList(studyBo.getCustomStudyId());

    List<ConsentInfoBo> consentInfoBoList = studyDao.getConsentInfoList(studyBo.getId());

    List<ConsentMasterInfoBo> consentMasterInfoList = studyDao.getConsentMasterInfoList();

    List<ComprehensionTestQuestionBo> comprehensionTestQuestionBoList =
        studyDao.getComprehensionTestQuestionList(studyBo.getId());

    List<QuestionnaireBo> questionnairesList =
        studyQuestionnaireDAO.getStudyQuestionnairesByStudyId(studyBo.getId());

    List<String> questionnaireIds = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(questionnairesList)) {
      for (QuestionnaireBo questionnaireBo : questionnairesList) {
        questionnaireIds.add(questionnaireBo.getId());
      }
    }

    List<QuestionnairesStepsBo> questionnairesStepsList =
        studyQuestionnaireDAO.getQuestionnairesStepsList(questionnaireIds);

    List<String> instructionFormIds = new ArrayList<>();
    List<String> stepTypeList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(questionnairesStepsList)) {
      for (QuestionnairesStepsBo questionnairesStepsBo : questionnairesStepsList) {
        instructionFormIds.add(questionnairesStepsBo.getInstructionFormId());
        stepTypeList.add(questionnairesStepsBo.getType());
      }
    }

    List<QuestionnairesFrequenciesBo> questionnairesFrequenciesBoList =
        studyQuestionnaireDAO.getQuestionnairesFrequenciesBoList(questionnaireIds);

    List<QuestionnaireCustomScheduleBo> questionnairesCustomFrequenciesBoList =
        studyQuestionnaireDAO.getQuestionnairesCustomFrequenciesBoList(questionnaireIds);

    List<QuestionsBo> questionsList =
        studyQuestionnaireDAO.getQuestionsByInstructionFormIds(instructionFormIds);

    List<FormMappingBo> formsList =
        studyQuestionnaireDAO.getFormMappingbyInstructionFormIds(instructionFormIds);

    List<InstructionsBo> instructionList =
        studyQuestionnaireDAO.getInstructionListByInstructionFormIds(instructionFormIds);

    List<QuestionResponseSubTypeBo> QuestionResponseSubTypeBoList =
        studyQuestionnaireDAO.getQuestionResponseSubTypeBoByInstructionFormIds(instructionFormIds);

    List<QuestionReponseTypeBo> QuestionResponseTypeBo =
        studyQuestionnaireDAO.getQuestionResponseTypeBoByInstructionFormIds(instructionFormIds);

    List<NotificationBO> notificationBOs = notificationDAO.getNotificationList(studyBo.getId());

    List<ResourceBO> resourceBOs = studyDao.getResourceList(studyBo.getId());

    List<ActiveTaskBo> activeTaskBos =
        studyActiveTasksDAO.getStudyActiveTaskByStudyId(studyBo.getId());

    List<String> activeTaskIds = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(activeTaskBos)) {
      for (ActiveTaskBo activeTaskBo : activeTaskBos) {
        activeTaskIds.add(activeTaskBo.getId());
      }
    }

    List<ActiveTaskAtrributeValuesBo> activeTaskAtrributeValuesBos =
        studyActiveTasksDAO.getActiveTaskAtrributeValuesByActiveTaskId(activeTaskIds);

    try {
      addStudiesInsertSql(studyBo, insertSqlStatements);
      addStudyPermissionInsertQuery(studyPermissionBo, insertSqlStatements);
      addStudySequenceInsertSql(studySequenceBo, insertSqlStatements);

      addStudyActiveTaskInsertSql(activeTaskBos, insertSqlStatements);
      addActiveTaskAtrributeValuesInsertSql(activeTaskAtrributeValuesBos, insertSqlStatements);

      addAnchorDateInsertSql(anchorDate, insertSqlStatements);
      addStudypagesListInsertSql(studypageList, insertSqlStatements);
      addEligibilityInsertSql(eligibilityBo, insertSqlStatements);
      addEligibilityTestListInsertSql(eligibilityBoList, insertSqlStatements);
      addConsentBoListInsertSql(consentBoList, insertSqlStatements);
      addConsentInfoBoListInsertSql(consentInfoBoList, insertSqlStatements);
      addConsentMasterInfoBoListInsertSql(consentMasterInfoList, insertSqlStatements);
      addComprehensionTestQuestionListInsertSql(
          comprehensionTestQuestionBoList, insertSqlStatements);
      addQuestionnaireBoListInsertSql(questionnairesList, insertSqlStatements);
      addQuestionnairesStepsListInsertSql(questionnairesStepsList, insertSqlStatements);
      addQuestionnaireFrequenciesBoInsertSql(questionnairesFrequenciesBoList, insertSqlStatements);
      addQuestionnaireCustomScheduleBoInsertSql(
          questionnairesCustomFrequenciesBoList, insertSqlStatements);
      addQuestionListInsertSql(questionsList, insertSqlStatements);
      addFormsListInsertSql(formsList, insertSqlStatements);
      addInstructionInsertSql(instructionList, insertSqlStatements);
      addQuestionsResponseSubTypeInsertSql(QuestionResponseSubTypeBoList, insertSqlStatements);
      addNotificationInsertSql(notificationBOs, insertSqlStatements);
      addResourceInsertSql(resourceBOs, insertSqlStatements);
      addStudyActiveTaskInsertSql(activeTaskBos, insertSqlStatements);
      addActiveTaskAtrributeValuesInsertSql(activeTaskAtrributeValuesBos, insertSqlStatements);

    } catch (SQLException e) {
      logger.error(String.format("export study failed due to %s", e.getMessage()), e);
    }
    for (String sql : insertSqlStatements) {
      System.out.println(sql);
    }

    return null;
  }

  private void addQuestionsResponseSubTypeInsertSql(
      List<QuestionResponseSubTypeBo> questionResponseSubTypeBoList,
      List<String> insertSqlStatements)
      throws SQLException {

    if (CollectionUtils.isEmpty(questionResponseSubTypeBoList)) {
      return;
    }

    List<String> questionResponseSubTypeBoInsertQueryList = new ArrayList<>();
    for (QuestionResponseSubTypeBo questionResponseSubTypeBo : questionResponseSubTypeBoList) {
      String questionResponseSubTypeBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.RESPONSE_SUB_TYPE_VALUE,
              questionResponseSubTypeBo.getResponseSubTypeValueId(),
              questionResponseSubTypeBo.getActive(),
              questionResponseSubTypeBo.getDescription(),
              questionResponseSubTypeBo.getDestinationStepId(),
              questionResponseSubTypeBo.getDetail(),
              questionResponseSubTypeBo.getExclusive(),
              questionResponseSubTypeBo.getImage(),
              questionResponseSubTypeBo.getResponseTypeId(),
              questionResponseSubTypeBo.getSelectedImage(),
              questionResponseSubTypeBo.getText(),
              questionResponseSubTypeBo.getValue(),
              questionResponseSubTypeBo.ver);
      questionResponseSubTypeBoInsertQueryList.add(questionResponseSubTypeBoInsertQuery);
    }
    insertSqlStatements.addAll(questionResponseSubTypeBoInsertQueryList);
  }

  private void addInstructionInsertSql(
      List<InstructionsBo> instructionList, List<String> insertSqlStatements) throws SQLException {
    List<String> instructionBoInsertQueryList = new ArrayList<>();
    if (CollectionUtils.isEmpty(instructionList)) {
      return;
    }

    for (InstructionsBo instructionBo : instructionList) {
      String instructionBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.INSTRUCTION,
              instructionBo.getId(),
              instructionBo.getActive(),
              instructionBo.getCreatedBy(),
              instructionBo.getCreatedOn(),
              instructionBo.getInstructionText(),
              instructionBo.getInstructionTitle(),
              instructionBo.getModifiedBy(),
              instructionBo.getModifiedOn(),
              instructionBo.getStatus());
      instructionBoInsertQueryList.add(instructionBoInsertQuery);
    }
    insertSqlStatements.addAll(instructionBoInsertQueryList);
  }

  private void addFormsListInsertSql(
      List<FormMappingBo> formsList, List<String> insertSqlStatements) throws SQLException {

    List<String> formMappingBoInsertQueryList = new ArrayList<>();
    if (CollectionUtils.isEmpty(formsList)) {
      return;
    }

    for (FormMappingBo formMappingBo : formsList) {
      String formMappingInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.FORM_MAPPING,
              formMappingBo.getId(),
              formMappingBo.getActive(),
              formMappingBo.getFormId(),
              formMappingBo.getQuestionId(),
              formMappingBo.getSequenceNo());
      formMappingBoInsertQueryList.add(formMappingInsertQuery);
    }
    insertSqlStatements.addAll(formMappingBoInsertQueryList);
  }

  private void addQuestionListInsertSql(
      List<QuestionsBo> questionsList, List<String> insertSqlStatements) throws SQLException {

    List<String> questionsBoInsertQueryList = new ArrayList<>();
    if (CollectionUtils.isEmpty(questionsList)) {
      return;
    }

    for (QuestionsBo questionBo : questionsList) {
      String questionInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.QUESTIONS,
              questionBo.getId(),
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
              questionBo.getStatDisplayUnits(),
              questionBo.getStatFormula(),
              questionBo.getStatShortName(),
              questionBo.getStatType(),
              questionBo.getStatus(),
              questionBo.getUseAnchorDate(),
              questionBo.getUseStasticData());
      questionsBoInsertQueryList.add(questionInsertQuery);
    }
    insertSqlStatements.addAll(questionsBoInsertQueryList);
  }

  private void addQuestionnaireCustomScheduleBoInsertSql(
      List<QuestionnaireCustomScheduleBo> questionnairesCustomFrequenciesBoList,
      List<String> insertSqlStatements)
      throws SQLException {
    List<String> questionnairesCustomScheduleBoInsertQueryList = new ArrayList<>();
    if (CollectionUtils.isEmpty(questionnairesCustomFrequenciesBoList)) {
      return;
    }

    for (QuestionnaireCustomScheduleBo questionnaireCustomScheduleBo :
        questionnairesCustomFrequenciesBoList) {
      String questionnairesCustomScheduleBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.QUESTIONNAIRES_CUSTOM_FREQUENCIES,
              questionnaireCustomScheduleBo.getId(),
              questionnaireCustomScheduleBo.getFrequencyEndDate(),
              questionnaireCustomScheduleBo.getFrequencyStartDate(),
              questionnaireCustomScheduleBo.getFrequencyTime(),
              questionnaireCustomScheduleBo.getQuestionnairesId(),
              questionnaireCustomScheduleBo.getTimePeriodFromDays(),
              questionnaireCustomScheduleBo.getTimePeriodToDays(),
              questionnaireCustomScheduleBo.isUsed(),
              questionnaireCustomScheduleBo.isxDaysSign(),
              questionnaireCustomScheduleBo.isyDaysSign());
      questionnairesCustomScheduleBoInsertQueryList.add(questionnairesCustomScheduleBoInsertQuery);
    }
    insertSqlStatements.addAll(questionnairesCustomScheduleBoInsertQueryList);
  }

  private void addQuestionnairesStepsListInsertSql(
      List<QuestionnairesStepsBo> questionnairesStepsList, List<String> insertSqlStatements)
      throws SQLException {
    if (CollectionUtils.isEmpty(questionnairesStepsList)) {
      return;
    }

    List<String> questionnaireStepsBoInsertQueryList = new ArrayList<>();
    for (QuestionnairesStepsBo questionnairesStepsBo : questionnairesStepsList) {
      String questionnaireStepsBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.QUESTIONNAIRES_STEPS,
              questionnairesStepsBo.getStepId(),
              questionnairesStepsBo.getActive(),
              questionnairesStepsBo.getCreatedBy(),
              questionnairesStepsBo.getCreatedOn(),
              questionnairesStepsBo.getDestinationStep(),
              questionnairesStepsBo.getInstructionFormId(),
              questionnairesStepsBo.getModifiedBy(),
              questionnairesStepsBo.getModifiedOn(),
              questionnairesStepsBo.getQuestionnairesId(),
              questionnairesStepsBo.getRepeatable(),
              questionnairesStepsBo.getRepeatableText(),
              questionnairesStepsBo.getSequenceNo(),
              questionnairesStepsBo.getSkiappable(),
              questionnairesStepsBo.getStatus(),
              questionnairesStepsBo.getStepShortTitle(),
              questionnairesStepsBo.getStepType());

      questionnaireStepsBoInsertQueryList.add(questionnaireStepsBoInsertQuery);
    }
    insertSqlStatements.addAll(questionnaireStepsBoInsertQueryList);
  }

  private void addStudiesInsertSql(StudyBo studyBo, List<String> insertSqlStatements)
      throws SQLException {

    if (studyBo == null) {
      return;
    }

    String studiesInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.STUDIES,
            studyBo.getId(),
            studyBo.getAppId(),
            studyBo.getCategory(),
            studyBo.getCreatedBy(),
            studyBo.getCreatedOn(),
            studyBo.getCustomStudyId(),
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
      StudySequenceBo studySequenceBo, List<String> insertSqlStatements) throws SQLException {

    if (studySequenceBo == null) {
      return;
    }

    String studySequeneInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.STUDY_SEQUENCE,
            studySequenceBo.getStudySequenceId(),
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
            studySequenceBo.getStudyId());
    insertSqlStatements.add(studySequeneInsertQuery);
  }

  private void addAnchorDateInsertSql(AnchorDateTypeBo anchorDate, List<String> insertSqlStatements)
      throws SQLException {

    if (anchorDate == null) {
      return;
    }

    String anchorDateTypeInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.ANCHORDATE_TYPE,
            anchorDate.getId(),
            anchorDate.getCustomStudyId(),
            anchorDate.getHasAnchortypeDraft(),
            anchorDate.getName(),
            anchorDate.getStudyId(),
            anchorDate.getVersion());

    insertSqlStatements.add(anchorDateTypeInsertQuery);
  }

  private void addStudypagesListInsertSql(
      List<StudyPageBo> studypageList, List<String> insertSqlStatements) throws SQLException {

    if (CollectionUtils.isEmpty(studypageList)) {
      return;
    }

    List<String> studyPageBoInsertQueryList = new ArrayList<>();
    for (StudyPageBo studyPageBo : studypageList) {
      String studyPageBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.STUDY_PAGE,
              studyPageBo.getPageId(),
              studyPageBo.getCreatedBy(),
              studyPageBo.getCreatedOn(),
              studyPageBo.getDescription(),
              studyPageBo.getImagePath(),
              studyPageBo.getModifiedBy(),
              studyPageBo.getModifiedOn(),
              studyPageBo.getStudyId(),
              studyPageBo.getTitle());

      studyPageBoInsertQueryList.add(studyPageBoInsertQuery);
    }
    insertSqlStatements.addAll(studyPageBoInsertQueryList);
  }

  private void addEligibilityInsertSql(
      EligibilityBo eligibilityBo, List<String> insertSqlStatements) throws SQLException {

    if (eligibilityBo == null) {
      return;
    }

    String eligibilityInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.ELIGIBILITY,
            eligibilityBo.getId(),
            eligibilityBo.getCreatedBy(),
            eligibilityBo.getCreatedOn(),
            eligibilityBo.getEligibilityMechanism(),
            eligibilityBo.getFailureOutcomeText(),
            eligibilityBo.getInstructionalText(),
            eligibilityBo.getModifiedBy(),
            eligibilityBo.getModifiedOn(),
            eligibilityBo.getStudyId());

    insertSqlStatements.add(eligibilityInsertQuery);
  }

  private void addNotificationInsertSql(
      List<NotificationBO> notificationBOs, List<String> insertSqlStatements) throws SQLException {

    if (CollectionUtils.isEmpty(notificationBOs)) {
      return;
    }
    List<String> notificationBoBoInsertQueryList = new ArrayList<>();
    for (NotificationBO notificationBO : notificationBOs) {

      String notificationBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.NOTIFICATION,
              notificationBO.getNotificationId(),
              notificationBO.getActiveTaskId(),
              notificationBO.isAnchorDate(),
              notificationBO.getAppId(),
              notificationBO.getCreatedBy(),
              notificationBO.getCreatedOn(),
              notificationBO.getCustomStudyId(),
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
              notificationBO.getQuestionnarieId(),
              notificationBO.getResourceId(),
              notificationBO.getScheduleDate(),
              notificationBO.getScheduleTime(),
              notificationBO.getStudyId(),
              notificationBO.getxDays(),
              notificationBO.getScheduleTimestamp());

      notificationBoBoInsertQueryList.add(notificationBoInsertQuery);
    }
    insertSqlStatements.addAll(notificationBoBoInsertQueryList);
  }

  private void addStudyActiveTaskInsertSql(
      List<ActiveTaskBo> activeTaskBos, List<String> insertSqlStatements) throws SQLException {

    if (CollectionUtils.isEmpty(activeTaskBos)) {
      return;
    }
    List<String> activeTaskBoInsertQueryList = new ArrayList<>();
    for (ActiveTaskBo activeTaskBo : activeTaskBos) {

      String activeTaskBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.ACTIVETASK_SQL,
              activeTaskBo.getId(),
              activeTaskBo.isAction(),
              activeTaskBo.getActive(),
              activeTaskBo.getActiveTaskLifetimeEnd(),
              activeTaskBo.getActiveTaskLifetimeStart(),
              activeTaskBo.getAnchorDateId(),
              activeTaskBo.getCreatedBy(),
              activeTaskBo.getCreatedDate(),
              activeTaskBo.getCustomStudyId(),
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
              activeTaskBo.getStudyId(),
              activeTaskBo.getTaskTypeId(),
              activeTaskBo.getTitle(),
              activeTaskBo.getVersion());

      activeTaskBoInsertQueryList.add(activeTaskBoInsertQuery);
    }
    insertSqlStatements.addAll(activeTaskBoInsertQueryList);
  }

  private void addActiveTaskAtrributeValuesInsertSql(
      List<ActiveTaskAtrributeValuesBo> activeTaskAttributeBos, List<String> insertSqlStatements)
      throws SQLException {

    if (CollectionUtils.isEmpty(activeTaskAttributeBos)) {
      return;
    }
    List<String> activeTaskAtrributeInsertQueryList = new ArrayList<>();
    for (ActiveTaskAtrributeValuesBo activeTaskAtrributeValuesBo : activeTaskAttributeBos) {

      String activeTaskAtrributeInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.ACTIVETASK_ATTRIBUTES_VALUES,
              activeTaskAtrributeValuesBo.getAttributeValueId(),
              activeTaskAtrributeValuesBo.getActive(),
              activeTaskAtrributeValuesBo.getActiveTaskId(),
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

      activeTaskAtrributeInsertQueryList.add(activeTaskAtrributeInsertQuery);
    }
    insertSqlStatements.addAll(activeTaskAtrributeInsertQueryList);
  }

  private void addResourceInsertSql(List<ResourceBO> resourceBOs, List<String> insertSqlStatements)
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
      StudyPermissionBO studyPermissionBo, List<String> insertSqlStatements) throws SQLException {

    if (studyPermissionBo == null) {
      return;
    }

    String studyPermissionsInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.STUDY_PERMISSION,
            studyPermissionBo.getStudyPermissionId(),
            studyPermissionBo.getDelFlag(),
            studyPermissionBo.getProjectLead(),
            studyPermissionBo.getStudyId(),
            studyPermissionBo.getUserId(),
            studyPermissionBo.isViewPermission());

    insertSqlStatements.add(studyPermissionsInsertQuery);
  }

  private void addEligibilityTestListInsertSql(
      List<EligibilityTestBo> eligibilityTestBoList, List<String> insertSqlStatements)
      throws SQLException {
    if (CollectionUtils.isEmpty(eligibilityTestBoList)) {
      return;
    }
    List<String> eligibilityTestBoInsertQueryList = new ArrayList<>();
    for (EligibilityTestBo eligibilityTestBo : eligibilityTestBoList) {

      String eligibilityTestBoBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.ELIGIBILITY_TEST,
              eligibilityTestBo.getId(),
              eligibilityTestBo.getActive(),
              eligibilityTestBo.getEligibilityId(),
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
      List<ConsentBo> consentBoList, List<String> insertSqlStatements) throws SQLException {

    if (CollectionUtils.isEmpty(consentBoList)) {
      return;
    }

    List<String> consentBoListInsertQuery = new ArrayList<>();
    for (ConsentBo consentBo : consentBoList) {
      String consentInsertSql =
          prepareInsertQuery(
              StudyExportSqlQueries.CONSENT,
              consentBo.getId(),
              consentBo.getAllowWithoutPermission(),
              consentBo.getComprehensionTestMinimumScore(),
              consentBo.getConsentDocContent(),
              consentBo.getConsentDocType(),
              consentBo.getCreatedBy(),
              consentBo.getCreatedOn(),
              consentBo.getCustomStudyId(),
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
              consentBo.getStudyId(),
              consentBo.getTaglineDescription(),
              consentBo.getTitle(),
              consentBo.getVersion(),
              consentBo.getEnrollAgain());

      consentBoListInsertQuery.add(consentInsertSql);
    }
    insertSqlStatements.addAll(consentBoListInsertQuery);
  }

  private void addConsentInfoBoListInsertSql(
      List<ConsentInfoBo> consentInfoBoList, List<String> insertSqlStatements) throws SQLException {

    if (CollectionUtils.isEmpty(consentInfoBoList)) {
      return;
    }

    List<String> consentInfoInsertQueryList = new ArrayList<>();
    for (ConsentInfoBo consentInfoBo : consentInfoBoList) {
      String consentInfoInsertSql =
          prepareInsertQuery(
              StudyExportSqlQueries.CONSENT_INFO,
              consentInfoBo.getId(),
              consentInfoBo.getActive(),
              consentInfoBo.getBriefSummary(),
              consentInfoBo.getConsentItemTitleId(),
              consentInfoBo.getConsentItemType(),
              consentInfoBo.getContentType(),
              consentInfoBo.getCreatedBy(),
              consentInfoBo.getCreatedOn(),
              consentInfoBo.getCustomStudyId(),
              consentInfoBo.getDisplayTitle(),
              consentInfoBo.getElaborated(),
              consentInfoBo.getHtmlContent(),
              consentInfoBo.getLive(),
              consentInfoBo.getModifiedBy(),
              consentInfoBo.getModifiedOn(),
              consentInfoBo.getSequenceNo(),
              consentInfoBo.getStatus(),
              consentInfoBo.getStudyId(),
              consentInfoBo.getUrl(),
              consentInfoBo.getVersion(),
              consentInfoBo.getVisualStep());

      consentInfoInsertQueryList.add(consentInfoInsertSql);
    }
    insertSqlStatements.addAll(consentInfoInsertQueryList);
  }

  private void addConsentMasterInfoBoListInsertSql(
      List<ConsentMasterInfoBo> consentMasterInfoBoList, List<String> insertSqlStatements)
      throws SQLException {

    if (CollectionUtils.isEmpty(consentMasterInfoBoList)) {
      return;
    }

    List<String> consentMasterInfoInsertQueryList = new ArrayList<>();
    for (ConsentMasterInfoBo consentMasterInfoBo : consentMasterInfoBoList) {
      String consentMasterInfoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.CONSENT_MASTER_INFO,
              consentMasterInfoBo.getId(),
              consentMasterInfoBo.getTitle(),
              consentMasterInfoBo.getType());

      consentMasterInfoInsertQueryList.add(consentMasterInfoInsertQuery);
    }
    insertSqlStatements.addAll(consentMasterInfoInsertQueryList);
  }

  private void addComprehensionTestQuestionListInsertSql(
      List<ComprehensionTestQuestionBo> comprehensionTestQuestionList,
      List<String> insertSqlStatements)
      throws SQLException {

    if (CollectionUtils.isEmpty(comprehensionTestQuestionList)) {
      return;
    }

    List<String> comprehensionTestQuestionInsertQueryList = new ArrayList<>();
    for (ComprehensionTestQuestionBo comprehensionTestQuestionBo : comprehensionTestQuestionList) {
      String comprehensionTestQuestionInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.COMREHENSION_TEST_QUESTIONS,
              comprehensionTestQuestionBo.getId(),
              comprehensionTestQuestionBo.getActive(),
              comprehensionTestQuestionBo.getCreatedBy(),
              comprehensionTestQuestionBo.getCreatedOn(),
              comprehensionTestQuestionBo.getModifiedBy(),
              comprehensionTestQuestionBo.getModifiedOn(),
              comprehensionTestQuestionBo.getQuestionText(),
              comprehensionTestQuestionBo.getSequenceNo(),
              comprehensionTestQuestionBo.getStatus(),
              comprehensionTestQuestionBo.getStructureOfCorrectAns(),
              comprehensionTestQuestionBo.getStudyId());

      comprehensionTestQuestionInsertQueryList.add(comprehensionTestQuestionInsertQuery);
    }
    insertSqlStatements.addAll(comprehensionTestQuestionInsertQueryList);
  }

  private void addQuestionnaireBoListInsertSql(
      List<QuestionnaireBo> questionnairesList, List<String> insertSqlStatements)
      throws SQLException {

    if (CollectionUtils.isEmpty(questionnairesList)) {
      return;
    }

    List<String> questionnairesBoInsertQueryList = new ArrayList<>();
    for (QuestionnaireBo questionnaireBo : questionnairesList) {
      String questionnairesBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.QUESTIONNAIRES,
              questionnaireBo.getId(),
              questionnaireBo.getActive(),
              questionnaireBo.getAnchorDateId(),
              questionnaireBo.getBranching(),
              questionnaireBo.getCreatedBy(),
              questionnaireBo.getCreatedDate(),
              questionnaireBo.getCustomStudyId(),
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
              questionnaireBo.getStudyId(),
              questionnaireBo.getStudyLifetimeEnd(),
              questionnaireBo.getStudyLifetimeStart(),
              questionnaireBo.getTitle(),
              questionnaireBo.getVersion());

      questionnairesBoInsertQueryList.add(questionnairesBoInsertQuery);
    }
    insertSqlStatements.addAll(questionnairesBoInsertQueryList);
  }

  private void addQuestionnaireFrequenciesBoInsertSql(
      List<QuestionnairesFrequenciesBo> questionnairesFrequenciesBoList,
      List<String> insertSqlStatements)
      throws SQLException {

    List<String> questionnairesFrequenciesBoInsertQueryList = new ArrayList<>();
    if (CollectionUtils.isEmpty(questionnairesFrequenciesBoList)) {
      return;
    }

    for (QuestionnairesFrequenciesBo questionnairesFrequenciesBo :
        questionnairesFrequenciesBoList) {
      String questionnairesFrequenciesBoInsertQuery =
          prepareInsertQuery(
              StudyExportSqlQueries.QUESTIONNAIRES_FREQUENCIES,
              questionnairesFrequenciesBo.getId(),
              questionnairesFrequenciesBo.getFrequencyDate(),
              questionnairesFrequenciesBo.getFrequencyTime(),
              questionnairesFrequenciesBo.getIsLaunchStudy(),
              questionnairesFrequenciesBo.getIsStudyLifeTime(),
              questionnairesFrequenciesBo.getQuestionnairesId(),
              questionnairesFrequenciesBo.getTimePeriodFromDays(),
              questionnairesFrequenciesBo.getTimePeriodToDays(),
              questionnairesFrequenciesBo.isxDaysSign(),
              questionnairesFrequenciesBo.isyDaysSign());
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
}
