import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './diagnostic-report.reducer';
import { Descriptions, Divider } from 'antd';
import { IDiagnosticReport } from 'app/shared/model/laboratory/diagnostic-report.model';
import { AdministrativeGender } from 'app/shared/model/enumerations/administrative-gender.model';
import { IAddress } from 'app/shared/model/laboratory/address.model';
import { IContact } from 'app/shared/model/laboratory/contact.model';

export const DiagnosticReportDetail = () => {
  const dispatch = useAppDispatch();

  const { diagnosticReportId, id } = useParams();

  useEffect(() => {
    dispatch(getEntity(diagnosticReportId));
  }, []);

  const diagnosticReportEntity = useAppSelector(state => state.laboratory.diagnosticReport.entity);

  const patientItems = [
    {
      key: 'id',
      label: 'Id',
      children: diagnosticReportEntity.id,
    },
  ];

  const getPatientItems = (diagnosticReport: IDiagnosticReport) => {
    const { subject } = diagnosticReport;
    return [
      {
        key: 'name',
        label: 'Name',
        children: subject?.name?.text,
      },
      {
        key: 'identifierType',
        label: 'Id Type',
        children: subject?.identifierType?.name,
      },
      {
        key: 'identifier',
        label: 'Id',
        children: subject?.identifier,
      },
      {
        key: 'gender',
        label: 'Gender',
        children: subject?.gender,
      },
      {
        key: 'birthDate',
        label: 'Birth Date',
        children: subject?.birthDate,
      },
      {
        key: 'address',
        label: 'Address',
        children: subject?.address?.text,
      },
    ];
  };

  const getItems = (diagnosticReport: IDiagnosticReport) => {
    const { format } = diagnosticReport;

    return format?.fieldFormats?.map((el, index) => {
      return {
        key: index,
        label: el.name,
        children: `${diagnosticReport?.fields ? diagnosticReport?.fields[el.name] : ''}`,
      };
    });
  };

  return (
    <Row>
      <Col>
        <h2 data-cy="diagnosticReportDetailsHeading">
          <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.detail.title">DiagnosticReport</Translate>
        </h2>
        <Descriptions bordered size={'small'} items={getPatientItems(diagnosticReportEntity)} />
        <Divider />
        <Descriptions title={diagnosticReportEntity?.format?.name} bordered items={getItems(diagnosticReportEntity)} column={4} />
        <Button tag={Link} to={`/laboratory/service-request/${id}`} replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/laboratory/diagnostic-report/${diagnosticReportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DiagnosticReportDetail;
