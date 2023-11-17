import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IServiceRequest } from 'app/shared/model/laboratory/service-request.model';
import { getEntities } from './service-request.reducer';
import { Empty } from 'antd';

export const ServiceRequest = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const serviceRequestList = useAppSelector(state => state.laboratory.serviceRequest.entities);
  const loading = useAppSelector(state => state.laboratory.serviceRequest.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="service-request-heading" data-cy="ServiceRequestHeading">
        <Translate contentKey="laboratoryApp.laboratoryServiceRequest.home.title">Service Requests</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="laboratoryApp.laboratoryServiceRequest.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/laboratory/service-request/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="laboratoryApp.laboratoryServiceRequest.home.createLabel">Create new Service Request</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {serviceRequestList && serviceRequestList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.status">Status</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.category">Category</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.priority.label">Priority</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.code">Code</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.doNotPerform">Do Not Perform</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.serviceId">Service Id</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.updatedAt">Updated At</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.updatedBy">Updated By</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryServiceRequest.deletedAt">Deleted At</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {serviceRequestList.map((serviceRequest, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/laboratory/service-request/${serviceRequest.id}`} color="link" size="sm">
                      {serviceRequest.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`laboratoryApp.ServiceRequestStatus.${serviceRequest.status}`} />
                  </td>
                  <td>{serviceRequest.category}</td>
                  <td>{serviceRequest.priority}</td>
                  <td>{serviceRequest.code}</td>
                  <td>{serviceRequest.doNotPerform ? 'true' : 'false'}</td>
                  <td>{serviceRequest.serviceId}</td>
                  <td>
                    {serviceRequest.createdAt ? (
                      <TextFormat type="date" value={serviceRequest.createdAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{serviceRequest.createdBy}</td>
                  <td>
                    {serviceRequest.updatedAt ? (
                      <TextFormat type="date" value={serviceRequest.updatedAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{serviceRequest.updatedBy}</td>
                  <td>
                    {serviceRequest.deletedAt ? (
                      <TextFormat type="date" value={serviceRequest.deletedAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/laboratory/service-request/${serviceRequest.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/laboratory/service-request/${serviceRequest.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/laboratory/service-request/${serviceRequest.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
        )}
      </div>
    </div>
  );
};

export default ServiceRequest;
