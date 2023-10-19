import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IValueSet } from 'app/shared/model/laboratory/value-set.model';
import { getEntities } from './value-set.reducer';

export const ValueSet = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const valueSetList = useAppSelector(state => state.laboratory.valueSet.entities);
  const loading = useAppSelector(state => state.laboratory.valueSet.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="value-set-heading" data-cy="ValueSetHeading">
        <Translate contentKey="laboratoryApp.laboratoryValueSet.home.title">Value Sets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="laboratoryApp.laboratoryValueSet.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/laboratory/value-set/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="laboratoryApp.laboratoryValueSet.home.createLabel">Create new Value Set</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {valueSetList && valueSetList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryValueSet.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryValueSet.name">Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {valueSetList.map((valueSet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/laboratory/value-set/${valueSet.id}`} color="link" size="sm">
                      {valueSet.id}
                    </Button>
                  </td>
                  <td>{valueSet.name}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/laboratory/value-set/${valueSet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/laboratory/value-set/${valueSet.id}/edit`}
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
                        to={`/laboratory/value-set/${valueSet.id}/delete`}
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
              <Translate contentKey="laboratoryApp.laboratoryValueSet.home.notFound">No Value Sets found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ValueSet;
