import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IValueSet } from 'app/shared/model/laboratory/value-set.model';
import { getEntity, updateEntity, createEntity, reset } from './value-set.reducer';
import { Button, Card, Col, DatePicker, Form, Input, InputNumber, Row, Select, Space, Switch } from 'antd';
import { useForm, useWatch } from 'antd/es/form/Form';
import { PlusOutlined, DeleteOutlined } from '@ant-design/icons';

import { DataType } from 'app/shared/model/enumerations/data-type.model';
import { IConstant } from 'app/shared/model/laboratory/constant.model';

export const ValueSetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const valueSetEntity = useAppSelector(state => state.laboratory.valueSet.entity);
  const loading = useAppSelector(state => state.laboratory.valueSet.loading);
  const updating = useAppSelector(state => state.laboratory.valueSet.updating);
  const updateSuccess = useAppSelector(state => state.laboratory.valueSet.updateSuccess);

  const [form] = useForm<IValueSet>();
  const dataType = useWatch('dataType', form);
  const constantsWatcher = useWatch('constants', form);

  const handleClose = () => {
    navigate('/laboratory/value-set');
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
      form.setFieldsValue({ dataType: DataType.STRING });
    } else {
      form.setFieldsValue({ dataType: valueSetEntity.dataType });
    }
  }, [valueSetEntity]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = (values: IValueSet) => {
    console.log(values);
    const entity = {
      ...valueSetEntity,
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
          ...valueSetEntity,
        };

  const filterOption = (input: string, option?: { label: string; value: string }) =>
    (option?.label ?? '').toLowerCase().includes(input.toLowerCase());

  const addValueField = () => {
    switch (dataType) {
      case DataType.BOOLEAN:
        return <Switch />;
      case DataType.DATE:
        return <DatePicker />;
      case DataType.STRING:
        return <Input placeholder={translate('laboratoryApp.laboratoryValueSet.create.values.value')} />;
      case DataType.INTEGER:
        return <InputNumber />;
      default:
        return <Input placeholder={translate('laboratoryApp.laboratoryValueSet.create.values.value')} />;
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col span="8">
          <h2 id="laboratoryApp.laboratoryValueSet.home.createOrEditLabel" data-cy="ValueSetCreateUpdateHeading">
            <Translate contentKey="laboratoryApp.laboratoryValueSet.home.createOrEditLabel">Create or edit a ValueSet</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col span="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <Form form={form} initialValues={defaultValues()} onFinish={saveEntity}>
              {!isNew ? (
                <Form.Item<IValueSet>
                  name="id"
                  hidden
                  label={translate('laboratoryApp.laboratoryValueSet.id')}
                  rules={[{ required: true }]}
                >
                  <Input readOnly hidden />
                </Form.Item>
              ) : null}
              <Form.Item<IValueSet>
                name="name"
                label={translate('laboratoryApp.laboratoryValueSet.create.name')}
                rules={[{ required: true, message: 'Please fill this field.' }]}
              >
                <Input placeholder={translate('laboratoryApp.laboratoryValueSet.create.name')} />
              </Form.Item>
              <Form.Item<IValueSet>
                name="description"
                label={translate('laboratoryApp.laboratoryValueSet.create.description')}
                rules={[{ required: true }]}
              >
                <Input placeholder={translate('laboratoryApp.laboratoryValueSet.create.description')} />
              </Form.Item>
              <Form.Item<IValueSet>
                name="dataType"
                label={translate('laboratoryApp.laboratoryValueSet.create.dataType')}
                rules={[{ required: true }]}
              >
                <Select
                  showSearch
                  placeholder="Select a data type "
                  optionFilterProp="children"
                  filterOption={filterOption}
                  options={Object.keys(DataType).map((key: string) => {
                    return { value: key, label: translate(`laboratoryApp.fieldFormatType.${key}`) };
                  })}
                />
              </Form.Item>
              <Form.List name={'constants'} initialValue={isNew ? [{ name: '', description: '', value: '' }] : constantsWatcher}>
                {(constants, { add, remove }) => (
                  <div style={{ display: 'flex', rowGap: 16, flexDirection: 'column' }}>
                    {constants.map(({ key, name, ...restField }) => (
                      <Card
                        size="small"
                        title={`Constant ${name + 1}`}
                        key={key}
                        extra={
                          <DeleteOutlined
                            onClick={() => {
                              remove(name);
                            }}
                            rev={undefined}
                          />
                        }
                      >
                        <Form.Item
                          {...restField}
                          name={[name, 'name']}
                          fieldKey={[key, 'name']}
                          label={translate('laboratoryApp.laboratoryValueSet.create.values.name')}
                          // rules={[{ required: true, message: 'Please fill this field!' }]}
                        >
                          <Input placeholder={translate('laboratoryApp.laboratoryValueSet.create.values.name')} />
                        </Form.Item>
                        <Form.Item
                          {...restField}
                          name={[name, 'description']}
                          fieldKey={[key, 'description']}
                          label={translate('laboratoryApp.laboratoryValueSet.create.values.description')}
                          // rules={[{ required: true, message: 'Please fill this field!' }]}
                        >
                          <Input placeholder={translate('laboratoryApp.laboratoryValueSet.create.values.description')} />
                        </Form.Item>
                        <Form.Item
                          {...restField}
                          name={[name, 'value']}
                          fieldKey={[key, 'value']}
                          label={translate('laboratoryApp.laboratoryValueSet.create.values.value')}
                          // rules={[{ required: true, message: 'Please fill this field!' }]}
                          valuePropName={dataType === DataType.BOOLEAN ? 'checked' : 'value'}
                        >
                          {addValueField()}
                        </Form.Item>
                      </Card>
                    ))}
                    <Button
                      type="dashed"
                      onClick={() => add({ name: '', description: '', value: '' })}
                      block
                      icon={<PlusOutlined rev={undefined} />}
                      className={'mb-4'}
                    >
                      Add field
                    </Button>
                  </div>
                )}
              </Form.List>
              <Button type="link" id="cancel-save" data-cy="entityCreateCancelButton" href="/laboratory/value-set" color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button type="primary" id="save-entity" data-cy="entityCreateSaveButton" htmlType="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </Form>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ValueSetUpdate;
