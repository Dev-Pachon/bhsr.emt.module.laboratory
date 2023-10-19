import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IIdentifierType } from 'app/shared/model/laboratory/identifier-type.model';
import { getEntity, updateEntity, createEntity, reset } from './identifier-type.reducer';

export const IdentifierTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const identifierTypeEntity = useAppSelector(state => state.laboratory.identifierType.entity);
  const loading = useAppSelector(state => state.laboratory.identifierType.loading);
  const updating = useAppSelector(state => state.laboratory.identifierType.updating);
  const updateSuccess = useAppSelector(state => state.laboratory.identifierType.updateSuccess);

  const handleClose = () => {
    navigate('/laboratory/identifier-type');
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
      ...identifierTypeEntity,
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
          ...identifierTypeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="laboratoryApp.laboratoryIdentifierType.home.createOrEditLabel" data-cy="IdentifierTypeCreateUpdateHeading">
            <Translate contentKey="laboratoryApp.laboratoryIdentifierType.home.createOrEditLabel">
              Create or edit a IdentifierType
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
                  id="identifier-type-id"
                  label={translate('laboratoryApp.laboratoryIdentifierType.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('laboratoryApp.laboratoryIdentifierType.name')}
                id="identifier-type-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/laboratory/identifier-type" replace color="info">
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

export default IdentifierTypeUpdate;
