import { Box, Fab, Tooltip } from '@mui/material';
import React from 'react';

export const FabButton = ({ Icon, onClick, tooltip = '', ...restProps }) => {
  return (
    <Box sx={{ textAlign: 'center', my: 2 }}>
      <Tooltip title={tooltip}>
        <Fab size="medium" onClick={onClick} {...restProps}>
          <Icon />
        </Fab>
      </Tooltip>
    </Box>
  );
};
