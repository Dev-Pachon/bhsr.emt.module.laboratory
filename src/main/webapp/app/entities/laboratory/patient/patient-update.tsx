import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPatient } from 'app/shared/model/laboratory/patient.model';
import { AdministrativeGender } from 'app/shared/model/enumerations/administrative-gender.model';
import { getEntity, updateEntity, createEntity, reset } from './patient.reducer';

export const PatientUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const patientEntity = useAppSelector(state => state.laboratory.patient.entity);
  const loading = useAppSelector(state => state.laboratory.patient.loading);
  const updating = useAppSelector(state => state.laboratory.patient.updating);
  const updateSuccess = useAppSelector(state => state.laboratory.patient.updateSuccess);
  const administrativeGenderValues = Object.keys(AdministrativeGender);

  const handleClose = () => {
    navigate('/laboratory/patient');
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
      ...patientEntity,
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
          gender: 'MALE',
          ...patientEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="laboratoryApp.laboratoryPatient.home.createOrEditLabel" data-cy="PatientCreateUpdateHeading">
            <Translate contentKey="laboratoryApp.laboratoryPatient.home.createOrEditLabel">Create or edit a Patient</Translate>
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
                  id="patient-id"
                  label={translate('laboratoryApp.laboratoryPatient.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('laboratoryApp.laboratoryPatient.active')}
                id="patient-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('laboratoryApp.laboratoryPatient.gender')}
                id="patient-gender"
                name="gender"
                data-cy="gender"
                type="select"
              >
                {administrativeGenderValues.map(administrativeGender => (
                  <option value={administrativeGender} key={administrativeGender}>
                    {translate('laboratoryApp.AdministrativeGender.' + administrativeGender)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('laboratoryApp.laboratoryPatient.birthDate')}
                id="patient-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/laboratory/patient" replace color="info">
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

export default PatientUpdate;
