package com.np.apisecurity.dto.response;

import java.util.List;

public record HexColorPaginationResponse(List<HexColor> colors,
                                         int currentPag,
                                         int size,
                                         int totalPages) {
}
