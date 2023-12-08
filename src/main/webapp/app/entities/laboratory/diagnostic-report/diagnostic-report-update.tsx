import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getPatients } from 'app/entities/laboratory/patient/patient.reducer';
import { getEntity as getValueSet } from 'app/entities/laboratory/value-set/value-set.reducer';
import { getEntities as getDiagnosticReportFormats } from 'app/entities/laboratory/diagnostic-report-format/diagnostic-report-format.reducer';
import { getEntities as getServiceRequests } from 'app/entities/laboratory/service-request/service-request.reducer';
import { DiagnosticReportStatus } from 'app/shared/model/enumerations/diagnostic-report-status.model';
import { createEntity, getEntity, reset, updateEntity } from './diagnostic-report.reducer';
import { IDiagnosticReport } from 'app/shared/model/laboratory/diagnostic-report.model';
import { Divider, Form, Typography, Button } from 'antd';
import DiagnosticReportUpdateField from 'app/entities/laboratory/diagnostic-report/diagnostic-report-update-field';
import DiagnosticReportPatientDescriptions from 'app/entities/laboratory/diagnostic-report/diagnostic-report-patient-descriptions';

export const DiagnosticReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id, diagnosticReportId } = useParams();

  const isNew = diagnosticReportId === undefined;

  const patients = useAppSelector(state => state.laboratory.patient.entities);
  const diagnosticReportFormats = useAppSelector(state => state.laboratory.diagnosticReportFormat.entities);
  const serviceRequests = useAppSelector(state => state.laboratory.serviceRequest.entities);
  const diagnosticReportEntity: IDiagnosticReport = useAppSelector(state => state.laboratory.diagnosticReport.entity);
  const loading = useAppSelector(state => state.laboratory.diagnosticReport.loading);
  const updating = useAppSelector(state => state.laboratory.diagnosticReport.updating);
  const updateSuccess = useAppSelector(state => state.laboratory.diagnosticReport.updateSuccess);
  const diagnosticReportStatusValues = Object.keys(DiagnosticReportStatus);

  const handleClose = () => {
    navigate('/laboratory/diagnostic-report');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(diagnosticReportId));
    }

    dispatch(getPatients({}));
    dispatch(getDiagnosticReportFormats({}));
    dispatch(getServiceRequests({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...diagnosticReportEntity,
      fields: Object.keys(values)?.map(el => {
        return { key: el, value: values[el] };
      }),
    };
    dispatch(updateEntity(entity));
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...diagnosticReportEntity,
          subject: diagnosticReportEntity?.subject?.id,
          format: diagnosticReportEntity?.format?.id,
          basedOn: diagnosticReportEntity?.basedOn?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Typography.Title
          level={2}
          style={{ textAlign: 'center' }}
          id="laboratoryApp.laboratoryDiagnosticReport.home.createOrEditLabel"
          data-cy="DiagnosticReportCreateUpdateHeading"
        >
          {/*<Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.home.createOrEditLabel">*/}
          {diagnosticReportEntity?.format?.name}
          {/*</Translate>*/}
        </Typography.Title>
      </Row>
      <Row className="justify-content-center">
        <Col>
          {loading ? (
            <p>Loading...</p>
          ) : (
            <>
              <DiagnosticReportPatientDescriptions subject={diagnosticReportEntity?.subject} />
              <Divider />
              <Form onFinish={saveEntity} initialValues={defaultValues()} style={{ width: '100%' }}>
                {diagnosticReportEntity?.format?.fieldFormats?.map((el, index) => (
                  <DiagnosticReportUpdateField key={index} el={el} index={index} />
                ))}
                <Link to={`/laboratory/service-request/${id}`}>
                  <FontAwesomeIcon icon="arrow-left" />
                  <Translate contentKey="entity.action.back">Back</Translate>
                </Link>
                &nbsp;
                <Button type="primary" id="save-entity" data-cy="entityCreateSaveButton" htmlType="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </Form>
            </>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DiagnosticReportUpdate;
