import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { translate } from 'react-jhipster';
import { Button, Form, Input, Row } from 'antd';
import { LeftOutlined, PlusOutlined } from '@ant-design/icons';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDiagnosticReportFormat } from 'app/shared/model/laboratory/diagnostic-report-format.model';
import { createEntity, getEntity, reset, updateEntity } from './diagnostic-report-format.reducer';
import { IFieldFormat } from 'app/shared/model/laboratory/field-format.model';
import { DataType } from 'app/shared/model/enumerations/data-type.model';
import { DiagnosticReportFormatField } from 'app/entities/laboratory/diagnostic-report-format/components/field-component';
import { getEntities as getValueSetEntities } from 'app/entities/laboratory/value-set/value-set.reducer';
import { useForm } from 'antd/es/form/Form';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import Swal from 'sweetalert2';
import { Save } from '@mui/icons-material';
import { Box, Fab } from '@mui/material';
import { FabButton } from 'app/entities/laboratory/shared/fab-button';

export const DiagnosticReportFormatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const diagnosticReportFormatEntity = useAppSelector(state => state.laboratory.diagnosticReportFormat.entity);
  const valueSetList = useAppSelector(state => state.laboratory.valueSet.entities);

  const loading = useAppSelector(state => state.laboratory.diagnosticReportFormat.loading);
  const updating = useAppSelector(state => state.laboratory.diagnosticReportFormat.updating);
  const updateSuccess = useAppSelector(state => state.laboratory.diagnosticReportFormat.updateSuccess);
  const [form] = useForm<IDiagnosticReportFormat>();

  const [fields, setFields] = useState<IFieldFormat[]>([]);
  const handleClose = () => {
    navigate('/laboratory/diagnostic-report-format');
  };

  useEffect(() => {
    dispatch(getValueSetEntities({}));
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
      setFields([...diagnosticReportFormatEntity.fieldFormats]);
    }
  }, [diagnosticReportFormatEntity]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = (values: IDiagnosticReportFormat) => {
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

  const handleOpenSave = (values: IDiagnosticReportFormat) => {
    Swal.fire({
      title: 'Guardar formato de reporte',
      text: '¿Deseas guardar el formato?',
      icon: 'info',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: '¡Si, quiero guardarlo!',
      cancelButtonText: 'Cancelar',
    }).then(result => {
      if (result.isConfirmed) {
        saveEntity(values);
      }
    });
  };

  const addField = () => {
    const newField: IFieldFormat = {
      name: '',
      dataType: DataType.STRING,
      defaultValue: 's',
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

  const defaultValues = () => {
    return isNew
      ? {}
      : {
          ...diagnosticReportFormatEntity,
        };
  };

  return (
    <>
      <PageHeader
        title={
          isNew
            ? translate('laboratoryApp.laboratoryDiagnosticReportFormat.home.createLabel')
            : translate('laboratoryApp.laboratoryDiagnosticReportFormat.home.editLabel')
        }
        leftAction={
          <Link to={`/laboratory/diagnostic-report-format`} style={{ placeSelf: 'end' }}>
            <LeftOutlined style={{ fontSize: '24px', color: 'white' }} rev={undefined} />
          </Link>
        }
      />
      <Row className="justify-content-center">
        {loading || diagnosticReportFormatEntity.length === 0 || fields.length === 0 ? (
          <p>Loading...</p>
        ) : (
          <Form
            form={form}
            layout="horizontal"
            initialValues={defaultValues()}
            onFinish={handleOpenSave}
            scrollToFirstError
            style={{ width: '100%' }}
          >
            {!isNew ? (
              <Form.Item<IDiagnosticReportFormat> name="id" hidden label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.id')}>
                <Input />
              </Form.Item>
            ) : null}
            <Form.Item<IDiagnosticReportFormat>
              name="name"
              label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.name')}
              rules={[{ required: true, message: '¡Este campo es requerido!' }]}
              labelCol={{ span: 8 }}
              wrapperCol={{ span: 8 }}
            >
              <Input placeholder={translate('laboratoryApp.laboratoryDiagnosticReportFormat.name')} />
            </Form.Item>
            {fields &&
              fields.length > 0 &&
              fields
                .sort((fieldA, fieldB) => fieldA.order - fieldB.order)
                .map((field: IFieldFormat, i: number) => (
                  <DiagnosticReportFormatField key={i} idx={i} onDelete={removeField} valueSet={valueSetList} />
                ))}
            <Button type="dashed" onClick={addField} block icon={<PlusOutlined rev={undefined} />} className={'mb-3'}>
              Agregar campo
            </Button>
            <FabButton Icon={Save} onClick={e => e} color={'info'} component={'button'} type={'submit'} tooltip={'Guardar'} />
          </Form>
        )}
      </Row>
    </>
  );
};

export default DiagnosticReportFormatUpdate;
