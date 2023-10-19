import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IIdentifierType } from 'app/shared/model/laboratory/identifier-type.model';
import { getEntities } from './identifier-type.reducer';

export const IdentifierType = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const identifierTypeList = useAppSelector(state => state.laboratory.identifierType.entities);
  const loading = useAppSelector(state => state.laboratory.identifierType.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="identifier-type-heading" data-cy="IdentifierTypeHeading">
        <Translate contentKey="laboratoryApp.laboratoryIdentifierType.home.title">Identifier Types</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="laboratoryApp.laboratoryIdentifierType.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/laboratory/identifier-type/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="laboratoryApp.laboratoryIdentifierType.home.createLabel">Create new Identifier Type</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {identifierTypeList && identifierTypeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryIdentifierType.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryIdentifierType.name">Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {identifierTypeList.map((identifierType, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/laboratory/identifier-type/${identifierType.id}`} color="link" size="sm">
                      {identifierType.id}
                    </Button>
                  </td>
                  <td>{identifierType.name}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/laboratory/identifier-type/${identifierType.id}`}
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
                        to={`/laboratory/identifier-type/${identifierType.id}/edit`}
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
                        to={`/laboratory/identifier-type/${identifierType.id}/delete`}
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
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="laboratoryApp.laboratoryIdentifierType.home.notFound">No Identifier Types found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default IdentifierType;
