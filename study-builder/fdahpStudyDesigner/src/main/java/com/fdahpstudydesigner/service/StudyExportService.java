package com.fdahpstudydesigner.service;

import com.fdahpstudydesigner.bo.AnchorDateTypeBo;
import com.fdahpstudydesigner.bo.ComprehensionTestQuestionBo;
import com.fdahpstudydesigner.bo.ConsentBo;
import com.fdahpstudydesigner.bo.ConsentInfoBo;
import com.fdahpstudydesigner.bo.ConsentMasterInfoBo;
import com.fdahpstudydesigner.bo.EligibilityBo;
import com.fdahpstudydesigner.bo.EligibilityTestBo;
import com.fdahpstudydesigner.bo.QuestionnaireBo;
import com.fdahpstudydesigner.bo.StudyBo;
import com.fdahpstudydesigner.bo.StudyPageBo;
import com.fdahpstudydesigner.bo.StudyPermissionBO;
import com.fdahpstudydesigner.bo.StudySequenceBo;
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

  @Autowired StudyDAO studyDao;

  @Autowired StudyQuestionnaireDAO studyQuestionnaireDAO;

  public String exportStudy(String studyId) {
    List<String> insertSqlStatements = new ArrayList<>();

    StudyBo studyBo = studyDao.getStudy(studyId);

    StudyPermissionBO studyPermissionBo =
        studyDao.getStudyPermissionBO(studyBo.getId(), studyBo.getUserId());

    StudySequenceBo studySequenceBo = studyDao.getStudySequenceByStudyId(studyBo.getId());
    AnchorDateTypeBo anchorDate = studyDao.getAnchorDateDetails(studyBo.getId());

    List<StudyPageBo> studypageList =
        studyDao.getOverviewStudyPagesById(studyBo.getId(), studyBo.getUserId());

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

    try {
      addStudiesInsertSql(studyBo, insertSqlStatements);
      addStudyPermissionInsertQuery(studyPermissionBo, insertSqlStatements);
      addStudySequenceInsertSql(studySequenceBo, insertSqlStatements);
      addAnchorDateInsertSql(anchorDate, insertSqlStatements);
      addStudypagesListInsertSql(studypageList, insertSqlStatements);
      addEligibilityInsertSql(eligibilityBo, insertSqlStatements);
    } catch (SQLException e) {
      logger.error(String.format("export study failed due to %s", e.getMessage()), e);
    }
    for (String sql : insertSqlStatements) {
      System.out.println(sql);
    }

    return null;
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
              studyPageBo.getTitle(),
              studyPageBo);

      insertSqlStatements.add(studyPageBoInsertQuery);
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

      insertSqlStatements.add(eligibilityTestBoBoInsertQuery);
    }
    insertSqlStatements.addAll(eligibilityTestBoInsertQueryList);
  }

  private void addConsentBoListInsertSql(
      List<ConsentBo> consentBoList, List<String> insertSqlStatements) throws SQLException {
    List<String> consentBoListInsertQuery = new ArrayList<>();
    for (ConsentBo consentBo : consentBoList) {
      String consent =
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

      insertSqlStatements.add(consent);
    }
    insertSqlStatements.addAll(consentBoListInsertQuery);
  }

  private void addConsentInfoBoListInsertSql(
      List<ConsentInfoBo> consentInfoBoList, List<String> insertSqlStatements) throws SQLException {
    List<String> studyPageBoInsertQueryList = new ArrayList<>();
    for (ConsentInfoBo consentInfoBo : consentInfoBoList) {
      String consentInfo =
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

      insertSqlStatements.add(consentInfo);
    }
    insertSqlStatements.addAll(studyPageBoInsertQueryList);
  }

  private void addConsentMasterInfoBoListInsertSql(
      List<ConsentMasterInfoBo> consentMasterInfoBoList, List<String> insertSqlStatements)
      throws SQLException {
    List<String> studyPageBoInsertQueryList = new ArrayList<>();
    for (ConsentMasterInfoBo consentMasterInfoBo : consentMasterInfoBoList) {
      String consentMasterInfo =
          prepareInsertQuery(
              StudyExportSqlQueries.CONSENT_MASTER_INFO,
              consentMasterInfoBo.getId(),
              consentMasterInfoBo.getTitle(),
              consentMasterInfoBo.getType());

      insertSqlStatements.add(consentMasterInfo);
    }
    insertSqlStatements.addAll(studyPageBoInsertQueryList);
  }

  private void addComprehensionTestQuestionListInsertSql(
      List<ComprehensionTestQuestionBo> comprehensionTestQuestionList,
      List<String> insertSqlStatements)
      throws SQLException {
    List<String> comprehensionTestQuestionInsertQueryList = new ArrayList<>();
    for (ComprehensionTestQuestionBo comprehensionTestQuestionBo : comprehensionTestQuestionList) {
      String consentMasterInfo =
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

      insertSqlStatements.add(consentMasterInfo);
    }
    insertSqlStatements.addAll(comprehensionTestQuestionInsertQueryList);
  }

  private void addQuestionnaireBoListInsertSql(
      List<QuestionnaireBo> questionnairesList, List<String> insertSqlStatements)
      throws SQLException {
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

      insertSqlStatements.add(questionnairesBoInsertQuery);
    }
    insertSqlStatements.addAll(questionnairesBoInsertQueryList);
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

  /* public static void main(String[] args) throws SQLException {
    StudyExportService export = new StudyExportService();

    String sqlQuery = StudyExportSqlQueries.STUDY_SEQUENCE;
    String columns = sqlQuery.substring(sqlQuery.indexOf('(') + 1, sqlQuery.indexOf(")"));

    System.out.println(columns);
     .replace("`", "")
    .split(",");

    //    String query = export.prepareInsertQuery(StudyExportSqlQueries.STUDIES, "1");
    //    System.out.println(query);
  }*/
}
