import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './diagnostic-report-format.reducer';

export const DiagnosticReportFormatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const diagnosticReportFormatEntity = useAppSelector(state => state.laboratory.diagnosticReportFormat.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="diagnosticReportFormatDetailsHeading">
          <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.detail.title">DiagnosticReportFormat</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.id">Id</Translate>
            </span>
          </dt>
          <dd>{diagnosticReportFormatEntity.id}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {diagnosticReportFormatEntity.createdAt ? (
              <TextFormat value={diagnosticReportFormatEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{diagnosticReportFormatEntity.createdBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {diagnosticReportFormatEntity.updatedAt ? (
              <TextFormat value={diagnosticReportFormatEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{diagnosticReportFormatEntity.updatedBy}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {diagnosticReportFormatEntity.deletedAt ? (
              <TextFormat value={diagnosticReportFormatEntity.deletedAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/laboratory/diagnostic-report-format" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/laboratory/diagnostic-report-format/${diagnosticReportFormatEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DiagnosticReportFormatDetail;
