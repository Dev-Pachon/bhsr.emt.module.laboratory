import React, { useState } from 'react';
import { Breadcrumb, Layout } from 'antd';
import Sider from 'antd/es/layout/Sider';
import { Content } from 'antd/es/layout/layout';
import Menu, { MenuProps } from 'antd/es/menu';
import ContainerOutlined from '@ant-design/icons/ContainerOutlined';
import PlusOutlined from '@ant-design/icons/PlusOutlined';
import LaptopOutlined from '@ant-design/icons/LaptopOutlined';
import NotificationOutlined from '@ant-design/icons/NotificationOutlined';
import { Navigate, Outlet, useNavigate } from 'react-router-dom';

export const LaboratoryHome = () => {
  const navigate = useNavigate();
  const [selectedKey, setSelectedKey] = useState(['getAll-diagnosticReport']);
  const rootSubmenuKeys = ['sub1', 'sub2'];
  const items2: MenuProps['items'] = [
    {
      key: 'format-management',
      label: 'Formats Management',
      subMenus: [
        {
          key: 'getAll-diagnosticReport',
          label: 'Home',
          href: '/laboratory/diagnostic-report-format',
          icon: ContainerOutlined,
        },
        {
          key: 'create-diagnosticReport',
          label: 'Create',
          href: '/laboratory/diagnostic-report-format/new',
          icon: PlusOutlined,
        },
      ],
    },
    {
      key: 'constants-management',
      label: 'Constants Management',
      subMenus: [
        { key: 'getAll-constants', label: 'Home', href: '/laboratory/value-set', icon: ContainerOutlined },
        { key: 'create-constant', label: 'Create', href: '/laboratory/value-set/new', icon: PlusOutlined },
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
