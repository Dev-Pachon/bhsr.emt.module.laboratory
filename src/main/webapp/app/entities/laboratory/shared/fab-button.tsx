import { Box, Fab } from '@mui/material';
import React from 'react';

export const FabButton = ({ Icon, onClick, ...restProps }) => {
  return (
    <Box sx={{ textAlign: 'center', my: 2 }}>
      <Fab size="medium" onClick={onClick} {...restProps}>
        <Icon />
      </Fab>
    </Box>
  );
};
