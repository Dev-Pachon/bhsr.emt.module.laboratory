import { Descriptions } from 'antd';
import { IPatient } from 'app/shared/model/laboratory/patient.model';
import React from 'react';
import { translate } from 'react-jhipster';

export const DiagnosticReportPatientDescriptions = ({ subject }) => {
  const getPatientItems = (subjectEl: IPatient) => {
    return [
      {
        key: 'name',
        label: 'Nombre',
        children: subjectEl?.name?.text,
      },
      {
        key: 'identifierType',
        label: 'Tipo de Identificación',
        children: subjectEl?.identifier?.type?.name,
      },
      {
        key: 'identifier',
        label: 'Número de Identificación',
        children: subjectEl?.identifier?.value,
      },
      {
        key: 'gender',
        label: 'Género',
        children: subjectEl?.gender && translate(`laboratoryApp.AdministrativeGender.${subjectEl?.gender}`),
      },
      {
        key: 'birthDate',
        label: 'Fecha de Nacimiento',
        children: subjectEl?.birthDate,
      },
    ];
  };

  return <Descriptions title={'Paciente'} bordered size={'small'} items={getPatientItems(subject)} />;
};

export default DiagnosticReportPatientDescriptions;
