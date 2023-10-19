import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './diagnostic-report.reducer';

export const DiagnosticReportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const diagnosticReportEntity = useAppSelector(state => state.laboratory.diagnosticReport.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="diagnosticReportDetailsHeading">
          <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.detail.title">DiagnosticReport</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.id">Id</Translate>
            </span>
          </dt>
          <dd>{diagnosticReportEntity.id}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.status">Status</Translate>
            </span>
          </dt>
          <dd>{diagnosticReportEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {diagnosticReportEntity.createdAt ? (
              <TextFormat value={diagnosticReportEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{diagnosticReportEntity.createdBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {diagnosticReportEntity.updatedAt ? (
              <TextFormat value={diagnosticReportEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{diagnosticReportEntity.updatedBy}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {diagnosticReportEntity.deletedAt ? (
              <TextFormat value={diagnosticReportEntity.deletedAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.subject">Subject</Translate>
          </dt>
          <dd>{diagnosticReportEntity.subject ? diagnosticReportEntity.subject.id : ''}</dd>
          <dt>
            <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.format">Format</Translate>
          </dt>
          <dd>{diagnosticReportEntity.format ? diagnosticReportEntity.format.id : ''}</dd>
          <dt>
            <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.basedOn">Based On</Translate>
          </dt>
          <dd>{diagnosticReportEntity.basedOn ? diagnosticReportEntity.basedOn.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/laboratory/diagnostic-report" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/laboratory/diagnostic-report/${diagnosticReportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DiagnosticReportDetail;
