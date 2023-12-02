import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getPatients } from 'app/entities/laboratory/patient/patient.reducer';
import { getEntities as getDiagnosticReportFormats } from 'app/entities/laboratory/diagnostic-report-format/diagnostic-report-format.reducer';
import { getEntities as getServiceRequests } from 'app/entities/laboratory/service-request/service-request.reducer';
import { DiagnosticReportStatus } from 'app/shared/model/enumerations/diagnostic-report-status.model';
import { createEntity, getEntity, reset, updateEntity } from './diagnostic-report.reducer';

export const DiagnosticReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const patients = useAppSelector(state => state.laboratory.patient.entities);
  const diagnosticReportFormats = useAppSelector(state => state.laboratory.diagnosticReportFormat.entities);
  const serviceRequests = useAppSelector(state => state.laboratory.serviceRequest.entities);
  const diagnosticReportEntity = useAppSelector(state => state.laboratory.diagnosticReport.entity);
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
      dispatch(getEntity(id));
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
      ...values,
      subject: patients.find(it => it.id.toString() === values.subject.toString()),
      format: diagnosticReportFormats.find(it => it.id.toString() === values.format.toString()),
      basedOn: serviceRequests.find(it => it.id.toString() === values.basedOn.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          status: 'REGISTERED',
          ...diagnosticReportEntity,
          subject: diagnosticReportEntity?.subject?.id,
          format: diagnosticReportEntity?.format?.id,
          basedOn: diagnosticReportEntity?.basedOn?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="laboratoryApp.laboratoryDiagnosticReport.home.createOrEditLabel" data-cy="DiagnosticReportCreateUpdateHeading">
            <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.home.createOrEditLabel">
              Create or edit a DiagnosticReport
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="diagnostic-report-id"
                  label={translate('laboratoryApp.laboratoryDiagnosticReport.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('laboratoryApp.laboratoryDiagnosticReport.status')}
                id="diagnostic-report-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {diagnosticReportStatusValues.map(diagnosticReportStatus => (
                  <option value={diagnosticReportStatus} key={diagnosticReportStatus}>
                    {translate('laboratoryApp.DiagnosticReportStatus.' + diagnosticReportStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('laboratoryApp.laboratoryDiagnosticReport.createdAt')}
                id="diagnostic-report-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryDiagnosticReport.createdBy')}
                id="diagnostic-report-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryDiagnosticReport.updatedAt')}
                id="diagnostic-report-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryDiagnosticReport.updatedBy')}
                id="diagnostic-report-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryDiagnosticReport.deletedAt')}
                id="diagnostic-report-deletedAt"
                name="deletedAt"
                data-cy="deletedAt"
                type="date"
              />
              <ValidatedField
                id="diagnostic-report-format"
                name="format"
                data-cy="format"
                label={translate('laboratoryApp.laboratoryDiagnosticReport.format')}
                type="select"
              >
                <option value="" key="0" />
                {diagnosticReportFormats
                  ? diagnosticReportFormats.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="diagnostic-report-basedOn"
                name="basedOn"
                data-cy="basedOn"
                label={translate('laboratoryApp.laboratoryDiagnosticReport.basedOn')}
                type="select"
              >
                <option value="" key="0" />
                {serviceRequests
                  ? serviceRequests.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/laboratory/diagnostic-report"
                replace
                color="info"
              >
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DiagnosticReportUpdate;
