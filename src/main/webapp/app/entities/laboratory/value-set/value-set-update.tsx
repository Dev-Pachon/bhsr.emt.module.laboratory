import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IValueSet } from 'app/shared/model/laboratory/value-set.model';
import { createEntity, getEntity, reset, updateEntity } from './value-set.reducer';
import { Button, Card, Col, Flex, Form, Input, Row, Select } from 'antd';
import { useForm, useWatch } from 'antd/es/form/Form';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';

import { DataType } from 'app/shared/model/enumerations/data-type.model';
import FormItemCustom from 'app/entities/laboratory/shared/FormItemCustom';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import { LeftOutlined } from '@ant-design/icons';

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

  return (
    <>
      <PageHeader
        title={
          isNew
            ? translate('laboratoryApp.laboratoryValueSet.home.createLabel')
            : translate('laboratoryApp.laboratoryValueSet.home.editLabel')
        }
        leftAction={
          <Link to={`/laboratory/value-set`} style={{ placeSelf: 'end' }}>
            <LeftOutlined style={{ fontSize: '24px', color: 'white' }} rev={undefined} />
          </Link>
        }
      />
      <Row className="justify-content-center">
        <Col span={24}>
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
              <Flex justify={'space-evenly'} align={'center'}>
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
              </Flex>
              <Form.List name={'constants'} initialValue={isNew ? [{ name: '', description: '', value: '' }] : constantsWatcher}>
                {(constants, { add, remove }) => (
                  <div style={{ display: 'flex', rowGap: 16, flexDirection: 'column' }}>
                    {constants.map(({ key, name, ...restField }) => (
                      <Card
                        className={'my-3'}
                        key={key}
                        title={`Constant ${name + 1}`}
                        extra={
                          name > 0 ? (
                            <DeleteOutlined
                              onClick={() => {
                                remove(name);
                              }}
                              rev={undefined}
                            />
                          ) : null
                        }
                      >
                        <Flex justify={'space-evenly'} align={'center'}>
                          <Form.Item
                            {...restField}
                            name={[name, 'name']}
                            fieldKey={[key, 'name']}
                            label={translate('laboratoryApp.laboratoryValueSet.create.values.name')}
                            rules={[{ required: true, message: 'Please fill this field!' }]}
                          >
                            <Input placeholder={translate('laboratoryApp.laboratoryValueSet.create.values.name')} />
                          </Form.Item>
                          <Form.Item
                            {...restField}
                            name={[name, 'description']}
                            fieldKey={[key, 'description']}
                            label={translate('laboratoryApp.laboratoryValueSet.create.values.description')}
                            rules={[{ required: true, message: 'Please fill this field!' }]}
                          >
                            <Input placeholder={translate('laboratoryApp.laboratoryValueSet.create.values.description')} />
                          </Form.Item>

                          <FormItemCustom
                            {...restField}
                            form={form}
                            name={[name, 'value']}
                            fieldKey={[key, 'value']}
                            label={translate('laboratoryApp.laboratoryValueSet.create.values.value')}
                            dataType={dataType}
                          />
                        </Flex>
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
              <Button type="primary" id="save-entity" data-cy="entityCreateSaveButton" htmlType="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </Form>
          )}
        </Col>
      </Row>
    </>
  );
};

export default ValueSetUpdate;
