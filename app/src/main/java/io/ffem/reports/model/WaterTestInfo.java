/*
 * Copyright (C) ffem (Foundation for Environmental Monitoring)
 *
 * This file is part of ffem reports
 *
 * ffem Reports is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * ffem Reports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ffem Reports. If not, see <http://www.gnu.org/licenses/>.
 */

package io.ffem.reports.model;

public class WaterTestInfo {
    public String testerName;
    public String phoneNumber;
    public String lake;
    public String location;
    public String date;
    public String time;
    public String geoLocation;
    public String nitrateResult;
    public String nitrateUnit;
    public String phosphateResult;
    public String phosphateUnit;
    public String pHResult;
    public String pHUnit = "";
    public String dissolvedOxygenResult;
    public String dissolvedOxygenUnit;
    public String[] values;
}
