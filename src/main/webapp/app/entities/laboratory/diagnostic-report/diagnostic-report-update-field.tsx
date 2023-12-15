import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getValueSets, reset } from 'app/entities/laboratory/value-set/value-set.reducer';
import React, { useEffect } from 'react';
import FormItemCustom from 'app/entities/laboratory/shared/FormItemCustom';
import { Form, Radio, Select } from 'antd';

export const DiagnosticReportUpdateField = ({ el, index, form }) => {
  const dispatch = useAppDispatch();
  const { valueSet } = el;
  const [custom, setCustom] = React.useState(false);

  const valueSetEntities = useAppSelector(state => state.laboratory.valueSet.entities);

  useEffect(() => {
    dispatch(getValueSets({}));
    return () => {
      dispatch(reset());
    };
  }, []);

  const handleModeChange = e => {
    setCustom(e.target.value);
  };

  const filterOption = (input: string, option?: { label: string; value: string }) =>
    (option?.label ?? '').toLowerCase().includes(input.toLowerCase());

  return (
    <>
      {valueSetEntities?.find(e => e.id === valueSet)?.constants ? (
        <>
          <Radio.Group onChange={handleModeChange} value={custom} style={{ marginBottom: 8 }}>
            <Radio.Button value={true}>Custom</Radio.Button>
            <Radio.Button value={false}>Select from constants</Radio.Button>
          </Radio.Group>

          {custom ? (
            <FormItemCustom form={form} name={el?.name} label={el?.name} dataType={el.dataType} />
          ) : (
            <Form.Item name={el?.name} label={el?.name} rules={[{ required: el?.isRequired }]}>
              <Select
                showSearch
                placeholder="Select a data type "
                optionFilterProp="children"
                filterOption={filterOption}
                options={valueSetEntities
                  ?.find(e => e.id === valueSet)
                  ?.constants?.map(constant => {
                    return { value: constant?.value, label: constant?.name };
                  })}
              />
            </Form.Item>
          )}
        </>
      ) : (
        <FormItemCustom form={form} name={el?.name} label={el?.name} dataType={el?.dataType} isRequired={el?.isRequired} />
      )}
    </>
  );
};

export default DiagnosticReportUpdateField;
