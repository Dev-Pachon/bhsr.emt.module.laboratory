import { DatePicker, Form, Input, InputNumber, Switch } from 'antd';
import { DataType } from 'app/shared/model/enumerations/data-type.model';
import React, { useEffect } from 'react';
import { translate } from 'react-jhipster';

const dateFormat = 'YYYY/MM/DD';
export const FormItemCustom = ({ form, name, dataType, isRequired = true, ...restProps }) => {
  useEffect(() => {
    form.resetFields([['constants', ...name]]);
  }, [dataType]);

  const addValueField = () => {
    switch (dataType) {
      case DataType.BOOLEAN:
        return <Switch />;
      case DataType.DATE:
        return <DatePicker format={dateFormat} />;
      case DataType.STRING:
        return <Input placeholder={translate('laboratoryApp.laboratoryValueSet.create.values.value')} />;
      case DataType.INTEGER:
        return <InputNumber />;
      case DataType.LONG_STRING:
        return <Input.TextArea autoSize={{ minRows: 2 }} />;
      // case DataType.FILE:
      //   return <Upload />;
      default:
        return <Input placeholder={translate('laboratoryApp.laboratoryValueSet.create.values.value')} />;
    }
  };
  return (
    <Form.Item
      {...restProps}
      name={name}
      rules={[{ required: isRequired, message: 'Please fill this field!' }]}
      // valuePropName={dataType === DataType.BOOLEAN ? 'checked' : dataType === DataType.FILE ? 'fileList' : 'value'}
      valuePropName={dataType === DataType.BOOLEAN ? 'checked' : 'value'}
    >
      {addValueField()}
    </Form.Item>
  );
};

export default FormItemCustom;
