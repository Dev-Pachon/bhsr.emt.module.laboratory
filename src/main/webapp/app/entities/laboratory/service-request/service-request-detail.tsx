import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './service-request.reducer';

export const ServiceRequestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const serviceRequestEntity = useAppSelector(state => state.laboratory.serviceRequest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="serviceRequestDetailsHeading">
          <Translate contentKey="laboratoryApp.laboratoryServiceRequest.detail.title">ServiceRequest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.id">Id</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.id}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.status">Status</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.status}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.category">Category</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.category}</dd>
          <dt>
            <span id="priority">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.priority">Priority</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.priority}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.code">Code</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.code}</dd>
          <dt>
            <span id="doNotPerform">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.doNotPerform">Do Not Perform</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.doNotPerform ? 'true' : 'false'}</dd>
          <dt>
            <span id="serviceId">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.serviceId">Service Id</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.serviceId}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {serviceRequestEntity.createdAt ? (
              <TextFormat value={serviceRequestEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.createdBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {serviceRequestEntity.updatedAt ? (
              <TextFormat value={serviceRequestEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.updatedBy}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="laboratoryApp.laboratoryServiceRequest.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {serviceRequestEntity.deletedAt ? (
              <TextFormat value={serviceRequestEntity.deletedAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/laboratory/service-request" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/laboratory/service-request/${serviceRequestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ServiceRequestDetail;
