import React from 'react';
import { Translate, translate, ValidatedField } from 'react-jhipster';
import { DataType } from 'app/shared/model/enumerations/data-type.model';
import { Row, Col, Checkbox, Form, Input, Select, Button, Switch, Card } from 'antd';
import { DeleteOutlined } from '@ant-design/icons';
import { IDiagnosticReportFormat } from 'app/shared/model/laboratory/diagnostic-report-format.model';

export const DiagnosticReportFormatField = ({ idx, onDelete }) => {
  const filterOption = (input: string, option?: { label: string; value: string }) =>
    (option?.label ?? '').toLowerCase().includes(input.toLowerCase());

  return (
    <Card
      className={'my-3'}
      title={`${translate('laboratoryApp.laboratoryDiagnosticReportFormat.update.field')} ${idx + 1}`}
      extra={
        idx > 0 ? (
          <DeleteOutlined
            onClick={() => {
              onDelete(idx);
            }}
            rev={undefined}
          />
        ) : null
      }
    >
      <Form.Item<IDiagnosticReportFormat>
        name={['fieldFormats', idx, 'name']}
        label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.name')}
        rules={[{ required: true, message: 'Please fill this field!' }]}
      >
        <Input placeholder={translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.name')} />
      </Form.Item>
      <Form.Item<IDiagnosticReportFormat>
        name={['fieldFormats', idx, 'dataType']}
        label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.dataType')}
        rules={[{ required: true, message: 'Please fill this field!' }]}
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
      <Form.Item<IDiagnosticReportFormat>
        name={['fieldFormats', idx, 'defaultValue']}
        label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.defaultValue')}
        rules={[{ required: true, message: 'Please fill this field!' }]}
      >
        <Input placeholder={translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.defaultValue')} />
      </Form.Item>
      <Form.Item<IDiagnosticReportFormat>
        name={['fieldFormats', idx, 'valueSet']}
        label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.valueSet')}
      >
        <Select
          showSearch
          allowClear
          placeholder="Select a dataset"
          optionFilterProp="children"
          filterOption={filterOption}
          options={Object.keys(DataType).map((key: string) => {
            return { value: key, label: translate(`laboratoryApp.fieldFormatType.${key}`) };
          })}
        />
      </Form.Item>
      <Form.Item<IDiagnosticReportFormat>
        name={['fieldFormats', idx, 'isRequired']}
        valuePropName="checked"
        label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.isRequired')}
      >
        <Switch></Switch>
      </Form.Item>
      <Form.Item<IDiagnosticReportFormat>
        name={['fieldFormats', idx, 'isSearchable']}
        label={translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.isSearchable')}
        valuePropName="checked"
      >
        <Switch></Switch>
      </Form.Item>
      <Form.Item<IDiagnosticReportFormat> name={['fieldFormats', idx, 'order']} hidden>
        <Input type="hidden" value={idx} />
      </Form.Item>
    </Card>
  );
};
