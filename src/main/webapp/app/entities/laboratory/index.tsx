import React, { useState } from 'react';
import { Breadcrumb, Layout } from 'antd';
import Sider from 'antd/es/layout/Sider';
import { Content } from 'antd/es/layout/layout';
import Menu, { MenuProps } from 'antd/es/menu';
import ContainerOutlined from '@ant-design/icons/ContainerOutlined';
import PlusOutlined from '@ant-design/icons/PlusOutlined';
import { Navigate, Outlet, useNavigate } from 'react-router-dom';
import { translate } from 'react-jhipster';

export const LaboratoryHome = () => {
  const navigate = useNavigate();
  const [selectedKey, setSelectedKey] = useState(['getAll-diagnosticReport']);
  const rootSubmenuKeys = ['sub1', 'sub2'];
  const items2: MenuProps['items'] = [
    {
      key: 'format-management',
      label: translate('laboratoryApp.laboratoryDiagnosticReportFormat.home.title'),
      subMenus: [
        {
          key: 'getAll-diagnosticReport',
          label: translate('global.menu.home'),
          href: '/laboratory/diagnostic-report-format',
          icon: ContainerOutlined,
        },
        {
          key: 'create-diagnosticReport',
          label: translate('global.create', {
            entity: translate('laboratoryApp.laboratoryDiagnosticReportFormat.home.title'),
          }),
          href: '/laboratory/diagnostic-report-format/new',
          icon: PlusOutlined,
        },
      ],
    },
    {
      key: 'constants-management',
      label: translate('laboratoryApp.laboratoryValueSet.home.title'),
      subMenus: [
        { key: 'getAll-constants', label: translate('global.menu.home'), href: '/laboratory/value-set', icon: ContainerOutlined },
        {
          key: 'create-constant',
          label: translate('global.create', {
            entity: translate('laboratoryApp.laboratoryValueSet.home.title'),
          }),
          href: '/laboratory/value-set/new',
          icon: PlusOutlined,
        },
      ],
    },
    // {
    //   key: 'settings',
    //   label: 'General settings',
    //   subMenus: [
    //     { key: 'getAll-settings', label: 'Settings', href: '/laboratory/identifier-type', icon: null },
    //     { key: 'create-settings', label: 'Create', href: '/laboratory/identifier-type/new', icon: null },
    //     { key: 'getAll-roles', label: 'Roles', href: '/laboratory/roles-management', icon: null },
    //   ],
    // },
  ].map((topMenu, index) => {
    const key = String(index + 1);

    return {
      key: `${key}`,
      label: `${topMenu.label}`,

      children: topMenu.subMenus.map((subMenu, j) => {
        return {
          key: subMenu.key,
          label: subMenu.label,
          icon: subMenu.icon && React.createElement(subMenu.icon),
          onClick() {
            navigate(subMenu.href);
            setSelectedKey([subMenu.key]);
          },
        };
      }),
    };
  });

  const [openKeys, setOpenKeys] = useState(['sub1']);

  const onOpenChange: MenuProps['onOpenChange'] = keys => {
    const latestOpenKey = keys.find(key => openKeys.indexOf(key) === -1);
    if (latestOpenKey && rootSubmenuKeys.indexOf(latestOpenKey!) === -1) {
      setOpenKeys(keys);
    } else {
      setOpenKeys(latestOpenKey ? [latestOpenKey] : []);
    }
  };

  return (
    <Layout>
      <Sider width={200}>
        <Menu
          mode="inline"
          selectedKeys={selectedKey}
          openKeys={openKeys}
          onOpenChange={onOpenChange}
          style={{ height: '100%', borderRight: 0 }}
          items={items2}
        />
      </Sider>
      <Layout style={{ padding: '0 24px 24px' }}>
        <Content
          style={{
            padding: 24,
            margin: 0,
            minHeight: 280,
          }}
        >
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};
