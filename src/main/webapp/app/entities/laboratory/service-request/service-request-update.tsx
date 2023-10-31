import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IServiceRequest } from 'app/shared/model/laboratory/service-request.model';
import { ServiceRequestStatus } from 'app/shared/model/enumerations/service-request-status.model';
import { getEntity, updateEntity, createEntity, reset } from './service-request.reducer';

export const ServiceRequestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const serviceRequestEntity = useAppSelector(state => state.laboratory.serviceRequest.entity);
  const loading = useAppSelector(state => state.laboratory.serviceRequest.loading);
  const updating = useAppSelector(state => state.laboratory.serviceRequest.updating);
  const updateSuccess = useAppSelector(state => state.laboratory.serviceRequest.updateSuccess);
  const serviceRequestStatusValues = Object.keys(ServiceRequestStatus);

  const handleClose = () => {
    navigate('/laboratory/service-request');
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
      ...serviceRequestEntity,
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
          status: 'DRAFT',
          ...serviceRequestEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="laboratoryApp.laboratoryServiceRequest.home.createOrEditLabel" data-cy="ServiceRequestCreateUpdateHeading">
            <Translate contentKey="laboratoryApp.laboratoryServiceRequest.home.createOrEditLabel">
              Create or edit a ServiceRequest
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
                  id="service-request-id"
                  label={translate('laboratoryApp.laboratoryServiceRequest.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('laboratoryApp.laboratoryServiceRequest.status')}
                id="service-request-status"
                name="status"
                data-cy="status"
                type="select"
                readOnly
              >
                {serviceRequestStatusValues.map(serviceRequestStatus => (
                  <option value={serviceRequestStatus} key={serviceRequestStatus}>
                    {translate('laboratoryApp.ServiceRequestStatus.' + serviceRequestStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('laboratoryApp.laboratoryServiceRequest.category')}
                id="service-request-category"
                name="category"
                data-cy="category"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryServiceRequest.priority')}
                id="service-request-priority"
                name="priority"
                data-cy="priority"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryServiceRequest.code')}
                id="service-request-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryServiceRequest.serviceId')}
                id="service-request-serviceId"
                name="serviceId"
                data-cy="serviceId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/laboratory/service-request" replace color="info">
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

export default ServiceRequestUpdate;
