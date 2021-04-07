package com.fdahpstudydesigner.service;

import com.fdahpstudydesigner.bo.AnchorDateTypeBo;
import com.fdahpstudydesigner.bo.EligibilityBo;
import com.fdahpstudydesigner.bo.StudyBo;
import com.fdahpstudydesigner.bo.StudyPageBo;
import com.fdahpstudydesigner.bo.StudyPermissionBO;
import com.fdahpstudydesigner.bo.StudySequenceBo;
import com.fdahpstudydesigner.dao.StudyDAO;
import com.fdahpstudydesigner.util.StudyExportSqlQueries;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudyExportService {

  @Autowired StudyDAO studyDao;

  public void exportStudy(String studyId) throws SQLException {
    List<String> insertSqlStatements = new ArrayList<>();

    StudyBo studyBo = studyDao.getStudy(studyId);

    addStudiesInsertSql(studyBo, insertSqlStatements);

    /*StudyPermissionBO studyPermissionBo =
        studyDao.getStudyPermissionBO(studyBo.getId(), studyBo.getUserId());
    StudySequenceBo studySequenceBo = studyDao.getStudySequenceByStudyId(studyBo.getId());
    AnchorDateTypeBo anchorDate = studyDao.getAnchorDateDetails(studyBo.getId());
    List<StudyPageBo> studypageList =
        studyDao.getOverviewStudyPagesById(studyBo.getId(), studyBo.getUserId());
    EligibilityBo eligibilityBo = studyDao.getStudyEligibiltyByStudyId(studyBo.getId());

    addStudyPermissionInsertQuery(studyPermissionBo, insertSqlStatements);
    addStudySequenceInsertSql(studySequenceBo, insertSqlStatements);
    addAnchorDateInsertSql(anchorDate, insertSqlStatements);
    addStudypagesListInsertSql(studypageList, insertSqlStatements);
    addEligibilityInsertSql(eligibilityBo, insertSqlStatements);*/
  }

  private void addStudiesInsertSql(StudyBo studyBo, List<String> insertSqlStatements)
      throws SQLException {

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
      /*if (values[i] instanceof Integer) {
        sqlQuery = sqlQuery.replaceFirst("<" + column + ">", values[i]);
      } else {*/
      sqlQuery = sqlQuery.replace("{" + column + "}", "'" + values[i] + "'");
      // }

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
