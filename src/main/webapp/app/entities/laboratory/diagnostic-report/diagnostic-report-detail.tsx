import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './diagnostic-report.reducer';
import { Descriptions, Divider } from 'antd';
import { IDiagnosticReport } from 'app/shared/model/laboratory/diagnostic-report.model';
import DiagnosticReportPatientDescriptions from 'app/entities/laboratory/diagnostic-report/diagnostic-report-patient-descriptions';

export const DiagnosticReportDetail = () => {
  const dispatch = useAppDispatch();

  const { diagnosticReportId, id } = useParams();

  useEffect(() => {
    dispatch(getEntity(diagnosticReportId));
  }, []);

  const diagnosticReportEntity = useAppSelector(state => state.laboratory.diagnosticReport.entity);

  const getItems = (diagnosticReport: IDiagnosticReport) => {
    const { format } = diagnosticReport;

    if (diagnosticReport.fields) {
      return diagnosticReport?.fields?.map((el, index) => {
        return {
          key: index,
          label: el?.key,
          children: `${el?.value || ''}`,
        };
      });
    } else {
      return format?.fieldFormats?.map((el, index) => {
        return {
          key: index,
          label: el.name,
          children: ``,
        };
      });
    }
  };

  return (
    <Row>
      <Col>
        <h2 data-cy="diagnosticReportDetailsHeading">
          <Link to={`/laboratory/service-request/${id}`}>
            <FontAwesomeIcon icon="arrow-left" />
          </Link>{' '}
          <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.detail.title">DiagnosticReport</Translate>
        </h2>
        <DiagnosticReportPatientDescriptions subject={diagnosticReportEntity?.subject} />
        <Divider />
        <Descriptions title={diagnosticReportEntity?.format?.name} bordered items={getItems(diagnosticReportEntity)} column={4} />
      </Col>
    </Row>
  );
};

export default DiagnosticReportDetail;
