import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './value-set.reducer';

export const ValueSetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const valueSetEntity = useAppSelector(state => state.laboratory.valueSet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="valueSetDetailsHeading">
          <Translate contentKey="laboratoryApp.laboratoryValueSet.detail.title">ValueSet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="laboratoryApp.laboratoryValueSet.id">Id</Translate>
            </span>
          </dt>
          <dd>{valueSetEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="laboratoryApp.laboratoryValueSet.name">Name</Translate>
            </span>
          </dt>
          <dd>{valueSetEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/laboratory/value-set" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/laboratory/value-set/${valueSetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ValueSetDetail;
