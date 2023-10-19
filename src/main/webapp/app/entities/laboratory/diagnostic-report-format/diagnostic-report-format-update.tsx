import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDiagnosticReportFormat } from 'app/shared/model/laboratory/diagnostic-report-format.model';
import { getEntity, updateEntity, createEntity, reset } from './diagnostic-report-format.reducer';

export const DiagnosticReportFormatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const diagnosticReportFormatEntity = useAppSelector(state => state.laboratory.diagnosticReportFormat.entity);
  const loading = useAppSelector(state => state.laboratory.diagnosticReportFormat.loading);
  const updating = useAppSelector(state => state.laboratory.diagnosticReportFormat.updating);
  const updateSuccess = useAppSelector(state => state.laboratory.diagnosticReportFormat.updateSuccess);

  const handleClose = () => {
    navigate('/laboratory/diagnostic-report-format');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...diagnosticReportFormatEntity,
      ...values,
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
          ...diagnosticReportFormatEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="laboratoryApp.laboratoryDiagnosticReportFormat.home.createOrEditLabel"
            data-cy="DiagnosticReportFormatCreateUpdateHeading"
          >
            <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.home.createOrEditLabel">
              Create or edit a DiagnosticReportFormat
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
                  id="diagnostic-report-format-id"
                  label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.createdAt')}
                id="diagnostic-report-format-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.createdBy')}
                id="diagnostic-report-format-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.updatedAt')}
                id="diagnostic-report-format-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.updatedBy')}
                id="diagnostic-report-format-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.deletedAt')}
                id="diagnostic-report-format-deletedAt"
                name="deletedAt"
                data-cy="deletedAt"
                type="date"
              />
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/laboratory/diagnostic-report-format"
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

export default DiagnosticReportFormatUpdate;
