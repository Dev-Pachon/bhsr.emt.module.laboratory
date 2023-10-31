import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Row, Col, Checkbox, Form, Input, Select, Button } from 'antd';
import { PlusOutlined } from '@ant-design/icons';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDiagnosticReportFormat } from 'app/shared/model/laboratory/diagnostic-report-format.model';
import { getEntity, updateEntity, createEntity, reset } from './diagnostic-report-format.reducer';
import { IFieldFormat } from 'app/shared/model/laboratory/field-format.model';
import { DataType } from 'app/shared/model/enumerations/data-type.model';
import { DiagnosticReportFormatField } from 'app/entities/laboratory/diagnostic-report-format/components/field-component';

export const DiagnosticReportFormatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const diagnosticReportFormatEntity = useAppSelector(state => state.laboratory.diagnosticReportFormat.entity);
  const loading = useAppSelector(state => state.laboratory.diagnosticReportFormat.loading);
  const updating = useAppSelector(state => state.laboratory.diagnosticReportFormat.updating);
  const updateSuccess = useAppSelector(state => state.laboratory.diagnosticReportFormat.updateSuccess);
  const [fields, setFields] = useState<IFieldFormat[]>([]);
  const handleClose = () => {
    navigate('/laboratory/diagnostic-report-format');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (isNew) {
      addField();
    } else {
      setFields(diagnosticReportFormatEntity.fieldFormats);
    }
  }, [diagnosticReportFormatEntity]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = (values: IDiagnosticReportFormat) => {
    console.log(values);
    const entity = {
      ...diagnosticReportFormatEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const addField = () => {
    const newField: IFieldFormat = {
      name: '',
      dataType: DataType.STRING,
      defaultValue: '',
      isRequired: false,
      isSearchable: false,
      order: fields.length + 1,
    };
    setFields([...fields, newField]);
  };

  const removeField = (idx: number) => {
    const newFields = [...fields];
    newFields.splice(idx, 1);
    setFields(newFields);
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...diagnosticReportFormatEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col span="8">
          <h2
            id="laboratoryApp.laboratoryDiagnosticReportFormat.home.createOrEditLabel"
            data-cy="DiagnosticReportFormatCreateUpdateHeading"
          >
            {isNew ? (
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.home.createLabel">
                Create a Diagnostic Report Format
              </Translate>
            ) : (
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.home.editLabel">
                Edit a Diagnostic Report Format
              </Translate>
            )}
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        {loading ? (
          <p>Loading...</p>
        ) : (
          <Form initialValues={defaultValues()} labelCol={{ span: 8 }} wrapperCol={{ span: 16 }} onFinish={saveEntity} scrollToFirstError>
            {!isNew ? (
              <Form.Item<IDiagnosticReportFormat> name="id" label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.id')}>
                <Input readOnly />
              </Form.Item>
            ) : null}
            <Form.Item<IDiagnosticReportFormat>
              name="name"
              label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.name')}
              rules={[{ required: true, message: 'Please fill this field!' }]}
            >
              <Input placeholder={translate('laboratoryApp.laboratoryDiagnosticReportFormat.name')} />
            </Form.Item>
            {fields &&
              fields.length > 0 &&
              fields
                .sort((fieldA, fieldB) => fieldA.order - fieldB.order)
                .map((field: IFieldFormat, i: number) => <DiagnosticReportFormatField key={i} idx={i} onDelete={removeField} />)}
            <Button type="dashed" onClick={addField} block icon={<PlusOutlined rev={undefined} />} className={'mb-3'}>
              Add field
            </Button>
            <Button
              type="link"
              id="cancel-save"
              data-cy="entityCreateCancelButton"
              href="/laboratory/diagnostic-report-format"
              color="info"
            >
              <FontAwesomeIcon icon="arrow-left" />
              &nbsp;
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.back">Back</Translate>
              </span>
            </Button>
            &nbsp;
            <Button id="save-entity" data-cy="entityCreateSaveButton" type="primary" htmlType={'submit'} disabled={updating}>
              <FontAwesomeIcon icon="save" />
              &nbsp;
              <Translate contentKey="entity.action.save">Save</Translate>
            </Button>
          </Form>
        )}
      </Row>
    </div>
  );
};

export default DiagnosticReportFormatUpdate;
